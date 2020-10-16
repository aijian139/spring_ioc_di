package com.yj.service;

import com.yj.dao.UserDao;
import com.yj.dao.UserDaoImpl;

public class UserService {

    private UserDaoImpl userDaoImpl;

    public UserDaoImpl getUserDaoImpl() {
        return userDaoImpl;
    }

    public void setUserDaoImpl(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    // 业务层调用持久层方法
    public void addUser(){
        System.out.println("业务逻辑层addUser()");
        userDaoImpl.addUser();
    };
}
