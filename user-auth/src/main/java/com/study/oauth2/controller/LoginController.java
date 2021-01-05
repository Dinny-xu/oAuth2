package com.study.oauth2.controller;

import com.study.oauth2.pojo.res.AuthToken;
import com.study.oauth2.service.LoginService;
import com.study.oauth2.util.CookieUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @program:
 * @description: 登录
 * @author: Xu·yan
 * @create: 2020-12-27 20:45
 **/
@Controller
@RequestMapping("/login")
public class LoginController {

    @Resource
    private LoginService loginService;

    @Value("${auth.cookieMaxAge}")
    private String cookieMaxAge;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @GetMapping
    public String login(String ReturnUrl, Model model) {
        model.addAttribute("ReturnUrl", ReturnUrl);
        return "index";
    }

    @PostMapping
    public String login(String userName, String password, String ReturnUrl) {
        // 返回值 令牌
        AuthToken authToken =  loginService.login(userName, password);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Cookie cookieRes = CookieUtil.addCookie(
                servletRequestAttributes.getResponse(),
                cookieDomain,
                "/",
                "uid",
                authToken.getJti(),
                Integer.parseInt(cookieMaxAge),
                false);
      /*  String value="12222";
        HttpPost post = new HttpPost("http://localhost");
        try {
            //创建参数集合
            List<BasicNameValuePair> list = new ArrayList<>();
            //添加参数
            list.add(new BasicNameValuePair("key", value));
            list.add(new BasicNameValuePair("releaseDate","2020-07-14 09:55:20"));
            //把参数放入请求对象，，post发送的参数list，指定格式
            post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            //添加请求头参数
            post.addHeader("timestamp","1594695607545");
            CloseableHttpClient client = HttpClients.createDefault();
            //启动执行请求，并获得返回值
            CloseableHttpResponse response = client.execute(post);
            //得到返回的entity对象
            HttpEntity entity = response.getEntity();
            //把实体对象转换为string
            String result = EntityUtils.toString(entity, "UTF-8");
            //返回内容
            System.out.println(result);
        } catch (Exception e1) {
            e1.printStackTrace();

        }*/
        //需要将短令牌 jti , 存储到Cookie -- 再次访问其他服务的时候就会携带着cookie 键为uid 值为jti 可以通过jti 获取到令牌
       /* Cookie cookie = new Cookie("uid", authToken.getJti());
        // 设置过期时间 目前采用 -1 规则 浏览器关闭则销毁用户信息
        cookie.setMaxAge(Integer.parseInt(cookieMaxAge));
        // 必须配置 /  用于各链接跳转携带参数
        cookie.setPath("/");
        // 设置顶级域名
        cookie.setDomain(".study.com"); // Tomcat容器 7.0 版本后 不支持域名前面加 .
        //是否选择保护对象
        cookie.setHttpOnly(false);*/

        String s = "redirect:" + "http://192.168.10.166";
        return s;
    }
}
