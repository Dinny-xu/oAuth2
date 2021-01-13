package com.study.oauth2.bean.res;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class AuthorizationUser extends User {


    /**
     * 性别1女 2男 3保密
     */
    private Integer gender;


    /**
     * ID
     */
    private String id;

    /**
     * 账号
     */
    private String username;

    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headThumbnail;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 用户身份 1.超级管理员 2.普通管理员 3.普通用户
     */
    private Integer identity;

    public AuthorizationUser(String id, String username,
                             String password,
                             String realName,
                             String nickName,
                             String headThumbnail,
                             Integer gender,
                             String telephone,
                             String email,
                             Date lastLoginTime,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.realName = realName;
        this.username = username;
        this.password = password;
        this.id = id;
        this.nickName = nickName;
        this.headThumbnail = headThumbnail;
        this.gender = gender;
        this.telephone = telephone;
        this.email = email;
        this.lastLoginTime = lastLoginTime;

    }


    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;


    /**
     *
     */
    private Date lastLoginTime;


}
