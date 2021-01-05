package com.study.oauth2.service.Impl;

import com.study.oauth2.pojo.res.AuthToken;
import com.study.oauth2.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program:
 * @description: 登录实现类
 * @author: Xu·yan
 * @create: 2020-12-28 10:31
 **/
@Service
public class LoginServiceImpl implements LoginService {

    private static String USER_AUTH = "user-auth";

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * auth:
     * ttl: 1200  #token存储到redis的过期时间
     * clientId: study	#客户端ID
     * clientSecret: studys	#客户端秘钥
     * cookieDomain: .study.com	#Cookie保存对应的域名
     * cookieMaxAge: -1			#Cookie过期时间，-1表示浏览器关闭则销毁
     */
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.ttl}")
    private String ttl;

    @Override
    public AuthToken login(String userName, String password) {

        //模拟postMan 发送请求

        /**
         * 1：注册中心获取微服务地址
         * 2：LoadBalancerClient
         * 3：Ribbon 负载均衡一个
         */
        //负载均衡处理器
        ServiceInstance choose = loadBalancerClient.choose(USER_AUTH);
        //访问链接
        String url = choose.getUri().toString() + "/oauth/token";

        // 设置请求头Authorization - 请求体 POST
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.set("Authorization",encodeUserNamePassword(clientId, clientSecret));
        // 设置 body 请求体
        MultiValueMap<String, String> body = new LinkedMultiValueMap();
        // oauth2 密码模式
        body.set("grant_type","password");
        body.set("username",userName);
        body.set("password",password);

        //请求实体
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(body,headers);
        // 执行提交
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map map = responseEntity.getBody();

        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(String.valueOf(map.get("access_token")));
        authToken.setRefreshToken(String.valueOf(map.get("refresh_token")));
        authToken.setJti(String.valueOf(map.get("jti")));

        // 将短令牌-长令牌 保存到redis 用于用户登录时网关连接进行判断
        stringRedisTemplate.boundValueOps(authToken.getJti()).set(authToken.getAccessToken(), Long.parseLong(ttl), TimeUnit.SECONDS);

        return authToken;
    }

    /**
     * 对用户名 密码 进行编码处理 类似postMan Authorization
     * TYPE : Basic Auth
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String encodeUserNamePassword(String clientId, String clientSecret) {

        String s = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(s.getBytes());

        // k : Authorization v : Basic

        return "Basic " + new String(encode);
    }
}
