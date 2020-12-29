package com.study.oauth2.service;


import com.study.oauth2.pojo.res.AuthToken;

public interface LoginService {

    AuthToken login(String userName, String password);
}
