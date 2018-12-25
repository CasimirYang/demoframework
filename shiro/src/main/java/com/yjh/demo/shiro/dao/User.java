package com.yjh.demo.shiro.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String userName;
    private String password;
    private String salt;

    public static User getDefault(){
        User user = new User();
        user.setPassword("123");
        user.setUserName("casi");
        return user;
    }

    public SysRole[] getRoles() {
        return new SysRole[0];
    }

    public byte[] getCredentialsSalt() {
        return salt.getBytes();
    }

    public void setSalt(String toHex) {
        this.salt = toHex;
    }
}
