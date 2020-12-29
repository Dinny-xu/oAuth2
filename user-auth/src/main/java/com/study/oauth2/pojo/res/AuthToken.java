package com.study.oauth2.pojo.res;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: CG
 * @description: 登录响应
 * @author: Xu·yan
 * @create: 2020-12-28 10:33
 **/
@Getter
@Setter
public class AuthToken {

    /**
     * 短令牌
     */
    private String jti;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * jwt 令牌
     */
    private String accessToken;


}
