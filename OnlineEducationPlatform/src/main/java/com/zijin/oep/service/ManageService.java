package com.zijin.oep.service;

import com.zijin.oep.model.CourseEx1;
import com.zijin.oep.model.School;
import com.zijin.oep.model.SystemLogEx;
import com.zijin.oep.model.User;

import java.util.List;

public interface ManageService {
    List<User> selectStudentByType();
    List<User> selectTeacherByType();
    List<School> selectAllSchools();
    void insertUser(User user);
    void disabledUser(String userId);
    void enableUser(String userId);
    List<CourseEx1> selectAlCourse();
    List<SystemLogEx> selectAllSystemLog();
}
