package com.study.user.interceptor;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class AccessLimitInterceptor implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String token = req.getHeader("token");

        Map<String, Object> param = new HashMap<>();
        param.put("token", token);
        String s = HttpUtil.get("http://localhost:9200/oauth/check_token", param);
        JSONObject jsonObject = JSONUtil.parseObj(s);
        if (Objects.equals(200, jsonObject.get("code", Integer.class))) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
