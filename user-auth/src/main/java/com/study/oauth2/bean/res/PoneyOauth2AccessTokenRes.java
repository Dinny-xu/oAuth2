package com.study.oauth2.bean.res;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author QinQiang
 * @date 2021/1/7 14:13
 **/
@Getter
@Setter
@Accessors(chain = true)
public class PoneyOauth2AccessTokenRes {

    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;
}
