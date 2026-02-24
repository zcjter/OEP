package com.zijin.oep.service;


import com.zijin.oep.model.User;

public interface UserService{
    User login(String id, String password);
}
