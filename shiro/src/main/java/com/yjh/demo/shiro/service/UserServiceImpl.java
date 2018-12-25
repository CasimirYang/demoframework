package com.yjh.demo.shiro.service;

import com.yjh.demo.shiro.dao.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findUserByName(String username) {
        User user = User.getDefault();
        PasswordHelper.encryptPassword(user);
        return user;
    }

    @Override
    public void saveUser(User user) {
        //todo
    }
}
