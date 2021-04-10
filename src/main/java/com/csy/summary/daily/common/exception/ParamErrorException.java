package com.csy.summary.daily.common.exception;

/**
 * <p>Description：自定义参数异常类</p>
 *
 * @author shuyun.cheng
 * @date 2020/7/28 16:10
 */
public class ParamErrorException extends RuntimeException {
    public ParamErrorException() {
    }

    public ParamErrorException(String msg) {
        super(msg);
    }
}
