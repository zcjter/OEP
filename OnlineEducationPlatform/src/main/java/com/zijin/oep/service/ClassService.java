package com.zijin.oep.service;

import com.zijin.oep.model.CourseClass;
import com.zijin.oep.model.Department;
import com.zijin.oep.model.SystemLogEx;
import com.zijin.oep.model.User;

import java.util.List;
import java.util.Map;

public interface ClassService {
    List<Department> fetchDepartments();
    List<CourseClass> fetchClasses(int cid);
    List<User> fetchClassStudents(int classId);

    List<User> searchStudentNotInClass(String searchText,int classId,int courseId);
    List<User> searchStudent(String searchText, int classId,String userId);
    List<User> studentsNotinClass(int classId,int courseId);
    List<User> studentsNotinClassByDepartment(int pid,int classId,int courseId);

    void createClass(CourseClass courseClass, String UserId);
    void renameClass(CourseClass courseClass,String UserId,int courseId);

    void addStudents(List<String> dataList, int classId,String userId,int courseId);
    void removeStudents(List<String> dataList, int classId, String userId,int courseId);
    List<Map<String, Object>> getLogStatisticsByStudent(int year, int month, String userId);
}
