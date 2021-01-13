package com.study.entity;



public enum ResultEnum {
    OK("message.200", "200", "执行成功"),
    ERROR("error.500", "500", "发生错误"),
    NOT_FOUND("error.404", "404", "没有找到资源"),
    NOT_LOGIN_ERROR("error.5000", "5000", "没有登录"),
    UNAUTHENTICATED_ERROR("error.5001", "5001", "没有认证"),
    CHECK_USER_INFO_FAIL("error.5002", "5002", "用户名或密码错误"),
    PWD_ERROR_TIMES_TOO_MANY("error.5003", "5003", "密码输入错误次数超过上线，请输入验证码"),
    PARAMS_VALID_FAIL("error.6000", "6000", "参数校验失败"),
    NULL_POINTER_EXCEPTION("error.7000", "7000", "系统发生错误!"),
    REST_CLIENT_EXCEPTION("error.8000", "8000", "国际化资源异常"),
    OTHER_ERROR("error.9999", "9999", "系统发生错误!");

    private String i18Code;
    private String code;
    private String msg;

    ResultEnum(String i18Code, String code, String msg) {
        this.i18Code = i18Code;
        this.code = code;
        this.msg = msg;
    }

    public String getI18Code() {
        return i18Code;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
