package com.winsum.chatliu.exception;


/**
 * 定义枚举 自定义错误类型
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_OK("问题提交失败"),
    QUESTION_NOT_FOUND("你找的问题不存在了，要不要换个试试？");

    @Override
    public String getMessage(){
        return message;
    }

    private String message;

    CustomizeErrorCode(String message){
        this.message = message;
    }
}
