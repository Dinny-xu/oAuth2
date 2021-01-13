package com.study.oauth2.exception;

import cn.hutool.core.lang.Console;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.Objects;


public class OAuth2ExceptionSerializer extends StdSerializer<Oauth2Exception> {

    public OAuth2ExceptionSerializer() {
        super(Oauth2Exception.class);
    }

    @Override
    public void serialize(Oauth2Exception e, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        int httpErrorCode = e.getHttpErrorCode();
        Console.log(httpErrorCode);
        generator.writeObjectField("code", httpErrorCode);
        generator.writeObjectField("success", Objects.equals(200, httpErrorCode) ? Boolean.TRUE : Boolean.FALSE);
        String message = e.getMessage();
        if (message != null) {
            message = HtmlUtils.htmlEscape(message);
        }
        generator.writeStringField("message", message);
        generator.writeStringField("data", null);
        generator.writeEndObject();
    }
}
