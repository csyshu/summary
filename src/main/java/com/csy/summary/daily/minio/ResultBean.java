package com.csy.summary.daily.minio;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author shuyun.cheng
 * @version 1.0
 * @date 2022-12-20 15:27
 */
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T> {
    private String msg;
    private int code;
    private T data;

    public ResultBean(String msg, T data) {
        this.msg = msg;
        this.code = 0;
        this.data = data;
    }

    public ResultBean(String msg) {
        this.msg = msg;
    }

    public static <T> ResultBean<T> ok(String msg, T data) {
        return new ResultBean(msg, data);
    }


    public static ResultBean error(String msg) {
        return new ResultBean(msg);
    }

    public static <T> ResultBean<T> error(String msg, T data) {
        return new ResultBean(msg, data);
    }

    public static ResultBean newInstance() {
        return new ResultBean<>();
    }
}
