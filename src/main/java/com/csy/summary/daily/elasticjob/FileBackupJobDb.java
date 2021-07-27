package com.csy.summary.daily.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shuyun.cheng
 */
@Component
@Slf4j
public class FileBackupJobDb implements SimpleJob {

    /**
     * 每次任务执行要备份文件的数量
     */
    private static final int FETCH_SIZE = 1;

    @Autowired
    private FileService fileService;

    /**
     * 任务执行代码逻辑
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("作业分片：{}", shardingContext.getShardingItem());
        //分片参数，（0=text,1=image,2=radio,3=vedio，参数就是text、image...）
        String jobParameter = shardingContext.getShardingParameter();
        //获取未备份的文件
        List<FileCustom> fileCustoms = fetchUnBackupFiles(jobParameter, FETCH_SIZE);
        //进行文件备份
        backupFiles(fileCustoms);
    }

    /**
     * 获取未备份的文件
     *
     * @param count 文件数量
     * @return List<FileCustom>
     */
    public List<FileCustom> fetchUnBackupFiles(String fileType, int count) {

        List<FileCustom> fileCustoms = fileService.fetchUnBackupFiles(fileType, count);
        log.info("time:{},获取文件{}个", LocalDateTime.now(), count);
        return fileCustoms;

    }

    /**
     * 文件备份
     *
     * @param files 文件信息集
     */
    public void backupFiles(List<FileCustom> files) {
        fileService.backupFiles(files);
    }
}