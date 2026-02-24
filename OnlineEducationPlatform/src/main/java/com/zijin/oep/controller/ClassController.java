package com.zijin.oep.controller;

import com.zijin.oep.model.CourseClass;
import com.zijin.oep.model.Department;
import com.zijin.oep.model.User;
import com.zijin.oep.result.R;
import com.zijin.oep.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 班级管理控制器
 */
@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * 获取部门列表信息
     * @return
     */
    @PostMapping("/get")
    public R  fetchDepartments(){
        List<Department> DepartmentList = classService.fetchDepartments();
        if(DepartmentList!=null){
            return R.ok().setData(DepartmentList);
        }else{
            return R.error();
        }
    }
    /**
     * 班级管理-->查询班级列表信息
     * @param courseId             //课程id
     * @return
     */
    @PostMapping("/list")
    public R fetchClasses(@RequestParam("courseId") int courseId){
        List<CourseClass> courseClassList = classService.fetchClasses(courseId);
        if(courseClassList !=null){
            return R.ok().setData(courseClassList);
        }else{
            return R.error();
        }
    }
    /**
     * 班级管理-->获取学生列表请求
     * @param classId          //班级id
     * @return
     */
    @PostMapping("/fetchClassStudents")
    public R fetchClassStudents(@RequestParam("classId") int classId){
        List<User> userList = classService.fetchClassStudents(classId);
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }

    /**
     * 班级管理-->搜索请求(根据姓名或学号模糊查询)
     * @param searchText          //搜索框
     * @param classId             //班级id
     * @param userId              //班级id
     * @return
     */
    @PostMapping("/searchStudent")
    public R searchStudent(@RequestParam("searchText") String searchText,
                           @RequestParam("classId") int classId,
                           @RequestParam("userId") String userId){
        List<User> userList = classService.searchStudent(searchText,classId,userId);
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }
    /**
     * 班级管理-->添加学生-->模糊查询不在课程里的班级学生请求
     * @param searchText           //搜索框
     * @param classId              //班级id
     * @param courseId            //课程id
     * @return
     */
    @PostMapping("/searchStudentNotInClass")
    public R searchStudentNotInClass(@RequestParam("searchText") String searchText,
                                     @RequestParam("classId") int classId,
                                     @RequestParam("courseId") int courseId){
        List<User> userList = classService.searchStudentNotInClass(searchText,classId,courseId);
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }

    /**
     * 班级管理-->添加学生-->查询不在课程班级的学生请求
     * @param classId               //班级id
     * @param courseId             //课程id
     * @return
     */
    @PostMapping("/studentsNotinClass")
    public R studentsNotinClass(@RequestParam("classId") int classId,
                                @RequestParam("courseId") int courseId){
        List<User> userList = classService.studentsNotinClass(classId,courseId);
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }

    /**
     * 班级管理-->添加学生-->按部门查询学生请求
     * @param deptId              //部门id
     * @param classId            //班级id
     * @param courseId          //课程id
     * @return
     */
    @PostMapping("/studentsNotinClassByDepartment")
    public R studentsNotinClassByDepartment(@RequestParam("deptId") int deptId,
                                            @RequestParam("classId") int classId,
                                            @RequestParam("courseId") int courseId) {
        List<User> userList = classService.studentsNotinClassByDepartment(deptId, classId,courseId);
        if (userList != null) {
            return R.ok().setData(userList);
        } else {
            return R.error();
        }
    }


    /**
     * 班级管理-->添加课程班级请求
     * @param className                   班级名称
     * @param courseId                    课程id
     * @param userId                      用户id
     * @return
     */
    @PostMapping("/add")
    public R createClass(@RequestParam("className") String className,
                        @RequestParam("courseId") int courseId,
                        @RequestParam("userId") String userId){
        CourseClass courseClass = new CourseClass();
        courseClass.setName(className);
        courseClass.setCid(courseId);
        classService.createClass(courseClass,userId);
        return R.ok("创建班级成功");
    }

    /**
     * 班级管理-->更新班级名称请求
     * @param classId               //班级id
     * @param className            //班级名称
     * @param userId               //用户id
     * @param courseId             //课程id
     * @return
     */
    @PostMapping("/update")
    public R renameClass(@RequestParam("classId") int classId,
                        @RequestParam("className") String className,
                        @RequestParam("userId") String userId,
                        @RequestParam("courseId") int courseId){
        CourseClass courseClass = new CourseClass();
        courseClass.setId(classId);
        courseClass.setName(className);
        classService.renameClass(courseClass,userId,courseId);
        return R.ok("修改班级成功");
    }

    /**
     * 班级管理-->添加班级学生请求(支持单个和批量)
     * @param dataList        //添加学生信息
     * @param classId         //班级id
     * @param userId          //用户id
     * @param courseId        //课程id
     * @return
     */
    @PostMapping("/addStudents")
    public R addStudents(@RequestParam("dataList") List<String> dataList,
                                @RequestParam("classId") int classId,
                                @RequestParam("userId") String userId,
                                @RequestParam("courseId") int courseId){
        classService.addStudents(dataList,classId,userId,courseId);
        return R.ok("添加班级学生成功");
    }

    /**
     * 班级管理-->移除学生请求(支持单个和批量)
     * @param dataList        //添加学生信息
     * @param classId         //班级id
     * @param userId          //用户id
     * @param courseId        //课程id
     * @return
     */
    @PostMapping("/removeStudent")
    public R removeStudents(@RequestParam("dataList") List<String> dataList,
                           @RequestParam("classId") int classId,
                           @RequestParam("userId") String userId,
                            @RequestParam("courseId") int courseId) {
        classService.removeStudents(dataList,classId,userId,courseId);
        return R.ok("删除成功");
    }

    /**
     * 班级管理-->统计学生访问日志请求
     * @param year             //年份
     * @param mouth           //月份
     * @param userId          //用户id
     * @return
     */
    @PostMapping("/getLogStatisticsByStudent")
    public R getLogStatisticsByStudent(@RequestParam("year") int year,
                                       @RequestParam("mouth") int mouth,
                                       @RequestParam("userId") String userId){
        List<Map<String, Object>> maps = classService.getLogStatisticsByStudent(year,mouth,userId);
        if(maps != null){
            return R.ok().setData(maps);
        }else {
            return R.error();
        }
    }
}
