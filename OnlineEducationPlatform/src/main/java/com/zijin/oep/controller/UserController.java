package com.zijin.oep.controller;

import com.zijin.oep.model.User;
import com.zijin.oep.request.UserLoginInRequest;
import com.zijin.oep.result.R;
import com.zijin.oep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录请求
     * @param userLoginInRequest
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody UserLoginInRequest userLoginInRequest) {
        String id = userLoginInRequest.getId();
        String password = userLoginInRequest.getPassword();
        User user = userService.login(id,password);
        if (user != null ) {
            if(user.getState()==1)
                return R.ok("登录成功").setData(user);
            else
                return R.error(20002,"该用户未启用");
        } else
            return R.error(20001, "用户名或密码错误");
    }
}
