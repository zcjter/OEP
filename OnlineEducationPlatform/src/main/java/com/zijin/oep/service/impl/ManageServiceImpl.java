package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.CourseMapper;
import com.zijin.oep.mapper.SchoolMapper;
import com.zijin.oep.mapper.SystemLogMapper;
import com.zijin.oep.mapper.UserMapper;
import com.zijin.oep.model.CourseEx1;
import com.zijin.oep.model.School;
import com.zijin.oep.model.SystemLogEx;
import com.zijin.oep.model.User;
import com.zijin.oep.service.ManageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {
    private UserMapper userMapper;//用户表mapper
    private SchoolMapper schoolMapper;//学校表mapper
    private CourseMapper courseMapper;//课程表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper

    public ManageServiceImpl(UserMapper userMapper,SchoolMapper schoolMapper,CourseMapper courseMapper,
                             SystemLogMapper systemLogMapper) {
        this.userMapper = userMapper;
        this.schoolMapper = schoolMapper;
        this.courseMapper = courseMapper;
        this.systemLogMapper = systemLogMapper;
    }

    /**
     * 搜索用户表中的学生记录
     * @return
     */
    @Override
    public List<User> selectStudentByType(){
        return userMapper.selectStudentByType();
    }

    /**
     * 搜索用户表中的教师记录
     * @return
     */
    @Override
    public List<User> selectTeacherByType(){
        return userMapper.selectTeacherByType();
    }
    /**
     * 查询学校列表
     * @return
     */
    @Override
    public List<School> selectAllSchools(){
        return schoolMapper.selectAll();
    }

    /**
     * 添加用户数据
     * @param user
     * @return
     */
    @Override
    public  void insertUser(User user){
       userMapper.insert(user);
    }

    /**
     * 禁用某一用户
     * @param userId
     */
    @Override
    public void disabledUser(String userId){
        userMapper.disabledUser(userId);
    }
    @Override
    /**
     * 启用某一用户
     * @param userId
     */
    public void enableUser(String userId){
        userMapper.enableUser(userId);
    }

    /**
     * 查询创建者课程信息
     * @return
     */
    @Override
    public List<CourseEx1> selectAlCourse(){
        return courseMapper.selectAlCourse();
    }

    /**
     * 查询系统日志信息
     * @return
     */
    public List<SystemLogEx> selectAllSystemLog(){
        return systemLogMapper.selectAllSystemLog();
    }
}
