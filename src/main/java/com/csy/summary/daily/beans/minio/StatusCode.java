package com.csy.summary.daily.beans.minio;

import lombok.Getter;

/**
 * @author csy
 * @version v1.0
 * @desc 状态码
 * @since 2022-12-26
 */
public enum StatusCode {
    /**
     * minio文件上传状态码
     */
    SUCCESS(20000, "操作成功"),
    PARAM_ERROR(40000, "参数异常"),
    NOT_FOUND(40004, "资源不存在"),
    FAILURE(50000, "系统异常"),
    CUSTOM_FAILURE(50001, "自定义异常错误"),
    ALONE_CHUNK_UPLOAD_SUCCESS(20001, "分片上传成功的标识"),
    ALL_CHUNK_UPLOAD_SUCCESS(20002, "所有的分片均上传成功");

    @Getter
    private final Integer code;
    @Getter
    private final String message;

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
