package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //获取不在cid班级的学生列表,用于AddStudent页面数据显示
    List<User> selectStudent(int classId,int courseId);

    //获取不在cid班级的指定pid部门的学生列表，用于AddStudent页面数据显示
    List<User> selectStudentByDepartment(int pid,int classId,int courseId);

    //获取指定cid班级的学生列表，用于前端ClassDetail页面数据显示
    List<User> selectStudentByClass(int classId);

    //获取指定cid班级的学生列表,用于前端ClassDetail页面数据模糊查询
    List<User> getStudentByNameOrNo(String searchText,int classId);

    //获取不在cid课程的教师列表,用于前端AddTeacher页面数据希纳是
    List<User> selectTeacher(int courseId);

    //获取指定cid课程的教师列表，用于前端TeacherManagement
    List<User> selectTeacherByCourse(int courseId);

    //获取不在cid课程的教师列表，用于前端AddTeacher页面的模糊查询
    List<User> getTeacherNotInCourseByNameOrNO(String searchText,int courseId);

    //获取不在cid班级的指定pid部门的教师列表,用于AddTeacher页面的模糊查询
    List<User> selectTeacherByDepartment(int pid,int courseId);

    //获取指定cid班级的学生列表,用于前端AddStudent页面数据模糊查询
    List<User> getStudentNotInCourseByNameOrNO(String searchText,int classId,int courseId);

    List<User> selectStudentInClass(int classId);
    List<User> selectStudentByType();
    List<User> selectTeacherByType();
    void disabledUser(String userId);
    void enableUser(String userId);
}
