package com.study.oauth2.config;

import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;


/**
 * oauth2 核心配置文件
 */
@Configuration
@EnableAuthorizationServer  //开启认证服务
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Resource
    private DataSource dataSource;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private PasswordEncoder passwordEncoder;



    /**
     *授权服务的安全配置
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
               // .passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    //将客户端信息存储到数据库
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 用户中心
     * 校验 应用是否 oauth_client_details 是否有此用户
     * 方式:   数据库(持久化保存用户信息)
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /*auth:
    ttl: 1200  #token存储到redis的过期时间
    clientId: study    #客户端ID
    clientSecret: study    #客户端秘钥
    cookieDomain: .study.com    #Cookie保存对应的域名
    cookieMaxAge: -1            #Cookie过期时间，-1表示浏览器关闭则销毁
    */
    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    /**
    * @description 加载配置文件
    * @author Xu·yan
    * @date 2020/12/27 8:30 下午
    */
    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    /**
     * 生成使用RSA方式加密的 Jwt令牌
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){


        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(
                        keyProperties.getKeyStore().getLocation(),
                        keyProperties.getKeyStore().getPassword().toCharArray());
        //钥匙对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(
                keyProperties.getKeyStore().getAlias(),
                keyProperties.getKeyStore().getSecret().toCharArray());

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }


    /**
     * 令牌保存
     * @return
     */
    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
        //return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 端点设置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //.tokenStore(tokenStore()) //保存令牌
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService) //校验 消费者信息 并授权
                .authenticationManager(authenticationManager);//认证管理器
    }
}
