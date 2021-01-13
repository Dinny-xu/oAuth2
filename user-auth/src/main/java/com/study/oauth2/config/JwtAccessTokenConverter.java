package com.study.oauth2.config;

import com.study.oauth2.bean.res.AuthorizationUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
* @description 除oAuth2 本身携带 username 到token中外，额外添加信息封装到token
* @author Xu·yan
* @date 2021/1/13 10:54 上午
*/
@Component
public class JwtAccessTokenConverter extends org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        AuthorizationUser user = (AuthorizationUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("userId", user.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return super.enhance(accessToken, authentication);
    }
}
