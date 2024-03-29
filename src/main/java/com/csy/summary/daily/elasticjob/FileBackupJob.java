package com.csy.summary.daily.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuyun.cheng
 */
@Slf4j
//@Component
public class FileBackupJob implements SimpleJob {

    /**
     * 每次任务执行要备份文件的数量
     */
    private static final int FETCH_SIZE = 1;
    /**
     * 文件列表（模拟）
     */
    public static List<FileCustom> files = new ArrayList<>();

    static {
        for (int i = 1; i < 11; i++) {
            FileBackupJob.files.add(new FileCustom(String.valueOf(i + 10), "文件" + (i + 10), "text", "content" + (i + 10)));
            FileBackupJob.files.add(new FileCustom(String.valueOf(i + 20), "文件" + (i + 20), "image", "content" + (i + 20)));
            FileBackupJob.files.add(new FileCustom(String.valueOf(i + 30), "文件" + (i + 30), "radio", "content" + (i + 30)));
            FileBackupJob.files.add(new FileCustom(String.valueOf(i + 40), "文件" + (i + 40), "video", "content" + (i + 40)));
        }
        log.info("生产测试数据完成");
    }

    //任务执行代码逻辑
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("作业分片：" + shardingContext.getShardingItem());
        //分片参数，（0=text,1=image,2=radio,3=vedio，参数就是text、image...）
        String jobParameter = shardingContext.getJobParameter();

        //获取未备份的文件
        List<FileCustom> fileCustoms = fetchUnBackupFiles(FETCH_SIZE);
        //进行文件备份
        backupFiles(fileCustoms);
    }

    /**
     * 获取未备份的文件
     *
     * @param count 文件数量
     * @return List<FileCustom>
     */
    public List<FileCustom> fetchUnBackupFiles(int count) {

        //获取的文件列表
        List<FileCustom> fileCustoms = new ArrayList<>();
        int num = 0;
        for (FileCustom fileCustom : files) {
            if (num >= count) {
                break;
            }
            if (!fileCustom.getBackedUp()) {
                fileCustoms.add(fileCustom);
                num++;
            }

        }
        log.info("time:{},获取文件{}个", LocalDateTime.now(), num);
        return fileCustoms;

    }

    /**
     * 文件备份
     *
     * @param files 文件信息集
     */
    public void backupFiles(List<FileCustom> files) {
        for (FileCustom fileCustom : files) {
            fileCustom.setBackedUp(true);
            log.info("time:{},备份文件，名称：{}，类型：{}", LocalDateTime.now(), fileCustom.getName(), fileCustom.getType());
        }
    }
}