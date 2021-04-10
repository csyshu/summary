package com.csy.summary.daily.common.enums;

/**
 * <p>Description：</p>
 *
 * @author shuyun.cheng
 * @date 2020/7/28 16:14
 */
public enum ResultEnum {

    /**
     * 结果枚举
     */
    SUCCESS(1000, "成功"),
    PARAM_ERROR(1001, "参数错误"),
    UNKNOWN_ERROR(9999, "未知错误");

    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
