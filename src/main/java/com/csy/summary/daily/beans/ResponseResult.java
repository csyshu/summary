package com.csy.summary.daily.beans;

import com.csy.summary.daily.common.enums.ResultEnum;
import lombok.Data;

/**
 * <p>Description：</p>
 *
 * @author shuyun.cheng
 * @date 2020/7/28 16:29
 */
@Data
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(ResultEnum enums) {
        this.code = enums.getCode();
        this.message = enums.getMessage();
    }

    public ResponseResult(ResultEnum enums, T data) {
        this.code = enums.getCode();
        this.message = enums.getMessage();
        this.data = data;
    }
}
