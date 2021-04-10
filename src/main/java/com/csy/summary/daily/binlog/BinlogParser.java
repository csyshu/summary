package com.csy.summary.daily.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogFileReader;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.ChecksumType;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

import java.io.File;
import java.io.IOException;

/**
 * <p>Description：解析mysql的binlog文件</p>
 *
 * @author shuyun.cheng
 * @date 2020/10/14 11:57
 */
public class BinlogParser {
    public static void main(String[] args) throws IOException {
        String filePath = "E:\\20201013_m21p124_binlog\\binlog.0001";
        int fileNum = 54;
        for (int i = 0; i < 14; i++) {
            String filePathNew = filePath + fileNum;
            File binlogFile = new File(filePathNew);
            EventDeserializer eventDeserializer = new EventDeserializer();
            eventDeserializer.setChecksumType(ChecksumType.CRC32);
            BinaryLogFileReader reader = new BinaryLogFileReader(binlogFile, eventDeserializer);

            for (Event event; (event = reader.readEvent()) != null; ) {
                EventHeader header = event.getHeader();
                EventType eventType = header.getEventType();
                if (EventType.QUERY == eventType) {
                    QueryEventData data = event.getData();
                    if ("test_db".equals(data.getDatabase())) {
                        String sql = data.getSql();
                        if (sql.startsWith("DELETE FROM test_table") || sql.startsWith("INSERT INTO test_table")) {
                            if (sql.contains("test")) {
                                System.out.println(filePathNew);
                                System.out.println(event);
                                System.out.println(sql);
                            }
                        }
                    }
                }
            }
            fileNum++;
            reader.close();
        }

    }
}
