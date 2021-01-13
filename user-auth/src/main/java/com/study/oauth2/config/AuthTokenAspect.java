package com.study.oauth2.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import com.study.entity.Result;
import com.study.oauth2.bean.res.PoneyOauth2AccessTokenRes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author Xu·yan
 * @description 处理统一返回对象
 * @date 2021/1/13 10:19 上午
 */
@Component
@Slf4j
@Aspect
public class AuthTokenAspect {

    /**
     * 默认调用oAuth2 获取token接口，oauth/token 返回值为json
     * 切面处理返回值
     * 统一响应  code :200
     *          success : true
     *          msg : 执行成功
     *          data - {
     *              "accessToken" :
     *              "refreshToken":
     *              "expiresIn" :
     *             }
     */
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    @SneakyThrows
    public Object handlePostAccessTokenMethod(ProceedingJoinPoint pjp) {
        Object proceed = pjp.proceed();
        if (Objects.nonNull(proceed)) {
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
            OAuth2AccessToken oAuth2AccessToken = responseEntity.getBody();
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                PoneyOauth2AccessTokenRes poneyOauth2AccessTokenRes = new PoneyOauth2AccessTokenRes()
                        .setAccessToken(oAuth2AccessToken.getValue())
                        .setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                        .setExpiresIn(oAuth2AccessToken.getExpiresIn());
                return ResponseEntity.status(200).body(Result.ok(poneyOauth2AccessTokenRes));
            }
        }
        return ResponseEntity.status(200).body(Result.error());
    }


    /**
     * 默认调用oAuth2 校验token接口，check/token 返回值为json
     *  切面处理返回值
     *  统一响应 code :200
     *  success : true
     *  msg : 执行成功
     *  data - {
     *          "expiresIn" : 剩余有效时间
     *          "active":
     *        }
     */
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint.checkToken(..))")
    @SneakyThrows
    public Object handleCheckAccessTokenMethod(ProceedingJoinPoint pjp) {
        Object proceed = pjp.proceed();
        if (Objects.nonNull(proceed)) {
            Map<String, Object> resultMap = (Map<String, Object>) proceed;
            if (MapUtil.isNotEmpty(resultMap)) {
                // 判断token有效期是否生效
                long exp = Convert.toLong(resultMap.get("exp")) - DateUtil.currentSeconds();
                if (exp > 0) {
                    Dict dict = Dict.create()
                            .set("expiresIn", exp)
                            .set("active", Convert.toBool(resultMap.get("active")));
                    return Dict.create().set("code", Result.ok().getCode())
                            .set("msg", Result.ok().getMsg())
                            .set("success", Result.ok().getSuccess())
                            .set("data", dict);
                } else {
                    return Dict.create().set("code", Result.error().getCode())
                            .set("msg", "token已过期")
                            .set("success", Result.error().getSuccess());
                }
            }
        }
        return Dict.create().set("code", Result.error().getCode())
                .set("msg", Result.error().getMsg())
                .set("success", Result.error().getSuccess());
    }
}