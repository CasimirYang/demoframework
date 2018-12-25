package com.yjh.demo.shiro.service;

import com.yjh.demo.shiro.dao.User;

public interface UserService {
    User findUserByName(String username);

    void saveUser(User user);
}
