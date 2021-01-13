package com.study.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
* @description 自定义异常处理器
* @author Xu·yan
* @date 2021/1/13 11:18 上午
*/
@JsonSerialize(using = OAuth2ExceptionSerializer.class)
public class Oauth2Exception extends OAuth2Exception {

    private Integer status;

    public Oauth2Exception(String message, Throwable t) {
        super(message, t);
        status = ((OAuth2Exception) t).getHttpErrorCode();
    }

    @Override
    public int getHttpErrorCode() {
        return status;
    }
}
