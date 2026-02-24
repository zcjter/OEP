package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.*;
import com.zijin.oep.model.*;
import com.zijin.oep.service.ClassService;
import com.zijin.oep.utils.UtilTools;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClassServiceImpl implements ClassService {
    private final CourseLogMapper courseLogMapper;
    private StudentClassMapper studentClassMapper;//班级学生关联表mapper
    private UserMapper userMapper;//用户表mapper
    private CourseClassMapper courseClassMapper;//课程班级表
    private DepartmentMapper departmentMapper;//部门表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper

    public ClassServiceImpl(StudentClassMapper studentClassMapper, UserMapper userMapper, CourseClassMapper courseClassMapper,
                            DepartmentMapper departmentMapper, SystemLogMapper systemLogMapper,
                            CourseLogMapper courseLogMapper){
        this.studentClassMapper = studentClassMapper;
        this.userMapper = userMapper;
        this.courseClassMapper = courseClassMapper;
        this.departmentMapper = departmentMapper;
        this.systemLogMapper = systemLogMapper;
        this.courseLogMapper = courseLogMapper;
    }

    /**
     * 统计班级部门信息
     * @return
     */
    @Override
    public List<Department> fetchDepartments() {
        return departmentMapper.selectAll();
    }
    /**
     * 查询课程班级列表
     * @param cid
     * @return
     */
    @Override
    public List<CourseClass> fetchClasses(int cid){
        return courseClassMapper.selectClass(cid);
    }
    /**
     * 查询班级学生信息
     * @param classId
     * @return
     */
    @Override
    public List<User> fetchClassStudents(int classId){
        return userMapper.selectStudentByClass(classId);
    }
    /**
     * 模糊查询不在班级里的学生信息
     * @param searchText
     * @param classId
     * @return
     */
    @Override
    public List<User> searchStudentNotInClass(String searchText,int classId,int courseId){
        return userMapper.getStudentNotInCourseByNameOrNO(searchText,classId,courseId);
    }
    /**
     * 模糊查询班级学生
     * @param searchText
     * @param classId
     * @return
     */
    @Override
    public List<User> searchStudent(String searchText, int classId,String userId){
        //往系统日志插入日志
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setDescr("查询在课程班级的学生操作");
        systemLogMapper.insert(systemLog);
        return  userMapper.getStudentByNameOrNo(searchText,classId);
    }
    /**
     * 班级管理-->添加学生-->查询不在课程班级里的学生信息
     * @param classId
     * @return
     */
    @Override
    public List<User> studentsNotinClass(int classId,int courseId){
        return userMapper.selectStudent(classId,courseId);
    }
    /**
     * 班级管理-->添加学生-->按部门查询学生
     * @param pid
     * @param classId
     * @return
     */
    @Override
    public List<User> studentsNotinClassByDepartment(int pid,int classId,int courseId){
        return userMapper.selectStudentByDepartment(pid,classId,courseId);
    }
    /**
     * 创建课程班级
     * @param courseClass
     * @param UserId
     */
    @Override
    public void createClass(CourseClass courseClass, String UserId){
        //创建课程班级
        courseClassMapper.insert(courseClass);

        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(UserId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setDescr("创建课程班级"+courseClass.getName());
        systemLog.setCourseid(courseClass.getCid());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 重命名班级名称
     * @param courseClass
     * @param UserId
     * @param courseId
     */
    @Override
    public void renameClass(CourseClass courseClass,String UserId,int courseId){
        //重命名班级名称
        courseClassMapper.rename(courseClass);

        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(UserId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("重命名课程班级"+courseClass.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 添加班级学生
     * @param dataList
     * @param classId
     * @param userId
     * @param courseId
     */
    public void addStudents(List<String> dataList, int classId,String userId,int courseId){
        StudentClass studentClass = new StudentClass();
        studentClass.setClassid(classId);
        for(String studentId:dataList){
            studentClass.setSid(studentId);
            studentClassMapper.insert(studentClass);
        }
        CourseClass courseClass = courseClassMapper.selectById(classId);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("往班级"+courseClass.getName()+"添加学生");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);

    }
    /**
     * 移除班级学生(多个)
     * @param dataList
     * @param classId
     * @param userId
     * @param courseId
     */
    public void removeStudents(List<String> dataList, int classId, String userId,int courseId){
        StudentClass studentClass = new StudentClass();
        studentClass.setClassid(classId);
        for(String studentId:dataList){
            studentClass.setSid(studentId);
            studentClassMapper.removeStudent(classId,studentClass.getSid());
        }
        CourseClass courseClass = courseClassMapper.selectById(classId);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("从班级"+courseClass.getName()+"移除学生");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 统计学生访问日志
     * @param year
     * @param month
     * @param userId
     * @return
     */
    public  List<Map<String, Object>> getLogStatisticsByStudent(int year, int month, String userId){
        return courseLogMapper.getLogStatisticsByStudent(year,month,userId);
    }
}


