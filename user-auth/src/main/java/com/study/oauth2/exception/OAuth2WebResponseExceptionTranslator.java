package com.study.oauth2.exception;

import cn.hutool.core.lang.Console;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;


@Component
public class OAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> translate(Exception e) {

        Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(e);
        Console.log(causeChain);
        Exception ase = (org.springframework.security.oauth2.common.exceptions.OAuth2Exception) this.throwableAnalyzer.getFirstThrowableOfType(org.springframework.security.oauth2.common.exceptions.OAuth2Exception.class, causeChain);
        //异常链中有OAuth2Exception异常
        if (ase != null) {
            return this.handleOAuth2Exception((org.springframework.security.oauth2.common.exceptions.OAuth2Exception) ase);
        }
        //身份验证相关异常
        ase = (AuthenticationException) this.throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }
        //异常链中包含拒绝访问异常
        ase = (AccessDeniedException) this.throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }
        //异常链中包含Http方法请求异常
        ase = (HttpRequestMethodNotSupportedException) this.throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
        }
        //异常链中包含Http方法请求异常
        ase = (InvalidTokenException) this.throwableAnalyzer.getFirstThrowableOfType(InvalidTokenException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new ValidTokenException(ase.getMessage(), ase));
        }
        return this.handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> handleOAuth2Exception(org.springframework.security.oauth2.common.exceptions.OAuth2Exception e) {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        Oauth2Exception exception = new Oauth2Exception(e.getMessage(), e);
        ResponseEntity<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> response = new ResponseEntity(exception, headers, HttpStatus.valueOf(status));
        return response;
    }

    private static class MethodNotAllowed extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }

        @Override
        public int getHttpErrorCode() {
            return 405;
        }
    }

    private static class UnauthorizedException extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "unauthorized";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }
    }

    private static class ValidTokenException extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
        public ValidTokenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "invalid_token";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }
    }

    private static class ServerErrorException extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }
    }

    private static class ForbiddenException extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "access_denied";
        }

        @Override
        public int getHttpErrorCode() {
            return 403;
        }
    }


}
