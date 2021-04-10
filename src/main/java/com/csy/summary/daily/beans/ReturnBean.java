package com.csy.summary.daily.beans;

import java.io.Serializable;

/**
 * @author shuyun.cheng@100credit.com
 * @date 2019/1/30 15:06
 */
public class ReturnBean implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = -2867756409591322259L;
    /**
     * 返回类型
     */
    private String returnType;
    /**
     * 返回结果
     */
    private Object returnValue;
    /**
     * 成功
     */
    private final String SUCCESS = "SUCCESS";
    /**
     * 失败
     */
    private final String FAIL = "FAIL";

    /**
     * 构造函数
     */
    public ReturnBean() {

    }

    public void setSuccessReturn(Object returnValue) {
        this.returnType = this.SUCCESS;
        this.returnValue = returnValue;
    }

    public void setFailReturn(Object returnValue) {
        this.returnType = this.FAIL;
        this.returnValue = returnValue;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "ReturnBean [returnType=" + returnType + ", returnValue=" + returnValue + ", SUCCESS=" + SUCCESS
                + ", FAIL=" + FAIL + "]";
    }

}

