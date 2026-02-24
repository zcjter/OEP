package com.zijin.oep.controller;

import com.zijin.oep.model.*;
import com.zijin.oep.result.R;
import com.zijin.oep.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理端控制器
 */
@RestController
@RequestMapping("/manage")
public class ManageController {
    @Autowired
    private ManageService manageService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 查询用户表中学生记录请求
     * @return
     */
    @PostMapping("/selectStudentByType")
    public R selectStudentByType(){
        List<User> userList = manageService.selectStudentByType();
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }

    /**
     * 查询用户表教师请求
     * @return
     */

    @PostMapping("/selectTeacherByType")
    public R selectTeacherByType(){
        List<User> userList = manageService.selectTeacherByType();
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }
    /**
     *查询学校信息请求
     * @return
     */
    @PostMapping("/get")
    public R selectAllSchools(){
        List<School> schoolList = manageService.selectAllSchools();
        if(schoolList!=null){
            return R.ok().setData(schoolList);
        }else{
            return R.error();
        }
    }

    /**
     * 插入用户信息请求
     * @param userId                 用户id
     * @param userName               用户名
     * @param password               用户密码
     * @param email                  用户邮箱
     * @param phone                  用户电话
     * @param state                  用户状态
     * @param schoolId               学校id
     * @param deptId                 部门id
     * @param type                   用户类型
     * @return
     */
    @PostMapping("/insert")
    public R insertUser(@RequestParam("userId") String userId,
                        @RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("email") String email,
                        @RequestParam("phone") String phone,
                        @RequestParam("state") int state,
                        @RequestParam("schoolId") int schoolId,
                        @RequestParam("deptId") int deptId,
                        @RequestParam("type") int type){
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setState(state);
        user.setPid(deptId);
        user.setType(type);
        user.setSid(schoolId);
        manageService.insertUser(user);
        return R.ok("添加成功");
    }

    /**
     * 禁用某一用户请求
     * @param userId
     * @return
     */
    @PostMapping("/disabledUser")
    public R disabledUser(@RequestParam("userId") String userId){
       manageService.disabledUser(userId);
       return R.ok("用户被禁用");
    }
    //启用某一用户
    @PostMapping("/enableUser")
    public R enableUser(@RequestParam("userId") String userId){
        manageService.enableUser(userId);
        return R.ok("用户被启用");
    }

    /**
     * 查询创建教师的课程信息请求
     * @return
     */
    @PostMapping("/selectAlCourse")
    public R selectAlCourse(){
        List<CourseEx1> courseList = manageService.selectAlCourse();
        if(courseList!=null){
            return R.ok().setData(courseList);
        }else{
            return R.error();
        }
    }

    /**
     * 查询系统日志信息请求
     * @return
     */
    @PostMapping("/selectAllSystemLog")
    public R selectAllSystemLog(){
        List<SystemLogEx> systemLogList = manageService.selectAllSystemLog();
        if(systemLogList!=null){
            return R.ok().setData(systemLogList);
        }else{
            return R.error();
        }
    }
}
