package com.winsum.chatliu.exception;


/**
 * 定义枚举 自定义错误类型
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_OK(2000,"问题提交失败"),
    QUESTION_NOT_FOUND(2001,"你找的问题不存在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未找到评论"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYSTEM_ERROR(2004,"服务器异常"),
    TYPE_PARAM_WRONG(2005,"评论类型错误"),
    COMMENT_NOT_FOUND(2006,"您回复的评论已消失"),
    COMMENT_NOT_NULL(2007,"评论不能为空")
    ;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
