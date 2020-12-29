package com.study.oauth2.controller;

import com.study.oauth2.pojo.res.AuthToken;
import com.study.oauth2.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

/**
 * @program: CG
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

    @GetMapping
    public String login(String ReturnUrl, Model model) {
        model.addAttribute("ReturnUrl", ReturnUrl);
        return "login";
    }

    @PostMapping
    public String login(String userName, String password, String ReturnUrl) {
        // 返回值 令牌
        AuthToken authToken =  loginService.login(userName, password);

        Cookie cookie = new Cookie("user-key", authToken.getJti());
        // 设置过期时间 目前采用 -1 规则 浏览器关闭则销毁用户信息
        cookie.setMaxAge(Integer.parseInt(cookieMaxAge));
        // 必须配置 /  用于各链接跳转携带参数
        cookie.setPath("/");
        // 设置顶级域名
        cookie.setDomain(".study.com"); // Tomcat容器 7.0 版本后 不支持域名前面加 .
        //是否选择保护对象
        cookie.setHttpOnly(false);

        return "redirect:" + ReturnUrl;
    }
}
