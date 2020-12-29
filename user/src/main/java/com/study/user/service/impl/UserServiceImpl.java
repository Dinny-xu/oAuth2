package com.study.user.service.impl;

import com.study.user.dao.UserMapper;
import com.study.user.pojo.User;
import com.study.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

}
