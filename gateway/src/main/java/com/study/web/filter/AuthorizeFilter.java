package com.study.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/****
 * @Author:xu`yan
 * @Description:
 * 判断用户是否登录
 *****/
@Component
public class AuthorizeFilter implements GlobalFilter,Ordered{


    //保存令牌
    public static final String UID = "uid";
//    public static final String URL = "//login.changgou.com:9200/login?ReturnUrl=";
    public static final String URL = "//http://192.168.10.166:9200/login?ReturnUrl=";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        /*VUE 保存令牌位置
         1）入参上
         2）请求头
         3）请求体中
         4） Cookie*/

        //1:判断是否登录
        HttpCookie cookie = request.getCookies().getFirst(UID);
        if(null != cookie){
            String jti = cookie.getValue();
            if(null != jti){
                //曾经登录过
                if(stringRedisTemplate.hasKey(jti)){
                    //存在令牌
                    //访问令牌
                    String token = stringRedisTemplate.boundValueOps(jti).get();
                    //request.getHeaders().set(); 不正确
                    request.mutate().header("Authorization","Bearer " + token);

                }else{
                    //过期 未登录
                    //   http://localhost:9200/login?ReturnUrl=  无令牌或过期, 自动重定向到登录页面
                    //1:响应  要求浏览器重定向  303
                    response.setStatusCode(HttpStatus.SEE_OTHER);

                    //设置响应头  Location:http://login.changgou.com:9200/login?ReturnUrl=
                    try {
                    response.getHeaders().set(HttpHeaders.LOCATION,URL +
                            URLEncoder.encode(request.getURI().getRawSchemeSpecificPart(),"utf-8"));
                    return response.setComplete();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                //未登录
                //   http://localhost:9200/login?ReturnUrl=http%3A%2F%2Fwww.changgou.com%2F
                //1:响应  要求浏览器重定向  303
                response.setStatusCode(HttpStatus.SEE_OTHER);

                //设置响应头  Location:http://login.changgou.com:9200/login?ReturnUrl=http://www.changgou.com/search?keywords=手机&brand=华为.....
                try {
                    response.getHeaders().set(HttpHeaders.LOCATION,URL +
                            URLEncoder.encode(request.getURI().getRawSchemeSpecificPart(),"utf-8"));
                    return response.setComplete();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }else{
            //未登录
            //   http://login.changgou.com:9200/login?ReturnUrl=http%3A%2F%2Fwww.changgou.com%2F
            //1:响应  要求浏览器重定向  303
            response.setStatusCode(HttpStatus.SEE_OTHER);

            //设置响应头  Location:http://login.changgou.com:9200/login?ReturnUrl=http://www.changgou.com/search?keywords=手机&brand=华为.....
            try {
                response.getHeaders().set(HttpHeaders.LOCATION,URL +
                        URLEncoder.encode(request.getURI().getRawSchemeSpecificPart(),"utf-8"));
                return response.setComplete();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}