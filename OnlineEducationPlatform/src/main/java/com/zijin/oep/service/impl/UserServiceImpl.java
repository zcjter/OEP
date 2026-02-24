package com.zijin.oep.service.impl;


import com.zijin.oep.mapper.SystemLogMapper;
import com.zijin.oep.mapper.UserMapper;
import com.zijin.oep.model.SystemLog;
import com.zijin.oep.model.User;
import com.zijin.oep.service.UserService;
import com.zijin.oep.utils.UtilTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserMapper userMapper;//用户表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper
    public UserServiceImpl(UserMapper userMapper,SystemLogMapper systemLogMapper) {
        this.userMapper = userMapper;
        this.systemLogMapper = systemLogMapper;
    }

    /**
     * 用户登录判断
     * @param id
     * @param password
     * @return
     */
    @Override
    public User login(String id, String password){
        User user = userMapper.selectById(id);
        //写日志
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(id);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setDescr("登录操作");
        systemLogMapper.insert(systemLog);

        if (user != null && passwordEncoder.matches(password,user.getPassword())) {
            return user;
        }
        return null;
    }

}
