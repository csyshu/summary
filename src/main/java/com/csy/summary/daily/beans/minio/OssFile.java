package com.csy.summary.daily.beans.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author csy
 * @version v1.0
 * @desc 文件信息类
 * @since 2022-12-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssFile {
    /**
     * OSS 存储时文件路径
     */
    private String ossFilePath;
    /**
     * 原始文件名
     */
    private String originalFileName;
}
