package com.study.user.service;



import com.study.user.pojo.User;

import java.util.List;

public interface UserService {

    /***
     * 查询所有
     * @return
     */
    List<User> findAll();


}
