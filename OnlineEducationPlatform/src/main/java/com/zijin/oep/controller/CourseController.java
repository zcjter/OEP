package com.zijin.oep.controller;


import com.zijin.oep.model.*;
import com.zijin.oep.result.R;
import com.zijin.oep.service.CourseService;

import com.zijin.oep.utils.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 课程控制器
 */
@RestController
@RequestMapping("/course")
public class CourseController {


    @Autowired
    private CourseService courseService;

    /**
     * 创建课程请求
     * @param courseName      //课程名称
     * @param teaId           //教师id
     * @param deptId          //部门id
     * @param descr           //课程描述
     * @param coverFile       //课程封面名
     * @return
     */

    @PostMapping("/create")
    public R createCourse(@RequestParam("courseName") String courseName,
                          @RequestParam("teaId") String teaId,
                          @RequestParam("deptId") int deptId,
                          @RequestParam("descr") String descr,
                          @RequestParam("coverFile") MultipartFile coverFile) {
        //创建课程
        Course course = new Course();
        course.setName(courseName);
        course.setUid(teaId);
        course.setPid(deptId);
        course.setSid(1);
        course.setDescr(descr);
        courseService.createCourse(coverFile,course,teaId);

        return R.ok("课程创建成功");

    }
    /**
     * 获取教师所教的课程列表请求
     * @param teacherId          //教师id
     * @return
     */
    @PostMapping("/fetchCourseList")
    public R fetchCourseList(@RequestParam("teacherId") String teacherId){
        List<Course> courseList = courseService.fetchCourseList(teacherId);
        if(courseList!=null){
            return R.ok().setData(courseList);
        }else {
            return R.error();
        }
    }

    /**
     * 获取教师所参与的课程列表请求
     * @param teacherId      //教师id
     * @return
     */
    @PostMapping("/fetchParticipateCourseList")
    public R fetchParticipateCourseList(@RequestParam("teacherId") String teacherId){
        List<Course> courseList=courseService.fetchParticipateCourseList(teacherId);
        if(courseList!=null){
            return R.ok().setData(courseList);
        }else {
            return R.error();
        }
    }

    /**
     * 获取学生参与课程章节内容请求
     * @param studentId          //学生id
     * @return
     */
    @PostMapping("/fetchStudentCourseList")
    public R fetchStudentCourseList(@RequestParam("studentId") String studentId){
        List<CourseEx> courseList = courseService.fetchStudentCourseList(studentId);
        if(courseList!=null){
            return R.ok().setData(courseList);
        }else {
            return R.error();
        }
    }

    /**
     * 获取指定课程章节列表请求
     * @param courseId                    //课程id
     * @return
     */
    @PostMapping("/chapter")
    public R fetchChapter(@RequestParam("courseId") int courseId){
        List<TreeNode> treeNodes = courseService.fetchChapter(courseId);
        if(treeNodes !=null){
            return R.ok().setData(treeNodes);
        }else{
            return R.error();
        }

    }

    /**
     * 获取指定章节教程内容信息请求
     * @param chapterId         //章节id
     * @param courseId          //课程id
     * @param classId           //班级id
     * @param userId           //用户id
     * @param role            //用户角色
     * @return
     */
    @PostMapping("/content")
    public R fetchChapterContent(@RequestParam("chapterId") int chapterId,
                                 @RequestParam("courseId") int courseId,
                                 @RequestParam("classId") int classId,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("userRole") int role){
        Content content = courseService.fetchChapterContent(chapterId,courseId,classId,userId,role);
        if(content !=null){
            return R.ok().setData(content);
        }else{
            return R.error();
        }
    }

    /**
     * 获取教师编辑章节内容请求
     * @param chapterId            //章节id
     * @param courseId             //课程id
     * @return
     */
    @PostMapping("/teacherContent")
    public R fetchChapterContent(@RequestParam("chapterId") int chapterId,
                                 @RequestParam("courseId") int courseId)
    {
        Content content = courseService.fetchChapterTeacherContent(chapterId,courseId);
        if(content !=null){
            return R.ok().setData(content);
        }else{
            return R.error();
        }
    }

    /**
     * 添加章节内容请求
     * @param chapterId              //章节id
     * @param courseId               //课程id
     * @param content                //课程内容
     * @param userId                 //用户id
     * @return
     */
    @PostMapping("/insertContent")
    public R insertContent(@RequestParam("chapterId") int chapterId,
                           @RequestParam("courseId") int courseId,
                           @RequestParam("content") String content,
                           @RequestParam("userId") String userId){
        Content contents = new Content();
        contents.setCid(chapterId);
        contents.setCourseid(courseId);
        contents.setContent(content);
        courseService.insertContent(chapterId,contents,userId);
        return R.ok();
    }

    /**
     * 更新章节内容请求
     * @param chapterId               //章节id
     * @param content                 //章节内容
     * @param userId                  //用户id
     * @param courseId               //课程id
     * @return
     */
    @PostMapping("updateContent")//更新章节内容
    public R updateContent(@RequestParam("chapterId") int chapterId,
                           @RequestParam("content") String content,
                           @RequestParam("userId") String userId,
                           @RequestParam("courseId") int courseId){
        courseService.updateContent(chapterId,content,userId,courseId);
        return R.ok();
    }

    /**
     * 调整指定章节位置请求
         * @param newParentId             //新章节位置
         * @param id                     //原章节位置
         * @param userId                //用户id
         * @param courseId              //课程id
         * @return
     */
    @PostMapping("/adjustChapter")
    public R adjustChapter(@RequestParam("newParentId") int newParentId,
                           @RequestParam("chapterId") int id,
                           @RequestParam("userId") String userId,
                           @RequestParam("courseId") int courseId){
        courseService.adjustChapter(newParentId,id,userId,courseId);
        return R.ok();
    }

    /**
     * 插入新章节请求
     * @param label            //章节名称
     * @param parentId         //章节父id
     * @param couserId         //课程id
     * @param type             //章节类型
     * @return
     */
    @PostMapping("/insertChapter")//插入新章节
    public R insertChapter(@RequestParam("label") String label,
                           @RequestParam("parentId") int parentId,
                           @RequestParam("courseId") int couserId,
                           @RequestParam("type") int type,
                           @RequestParam("userId") String userId){
        Chapter chapter = new Chapter();
        chapter.setLabel(label);
        chapter.setPid(parentId);
        chapter.setCourseid(couserId);
        chapter.setType(type);
        courseService.insertChapter(chapter,userId);
        return R.ok("添加章节成功");
    }

    /**
     * 重命名指定章节名称请求
     * @param name                章节名称
     * @param id                  章节id
     * @param userId              用户id
     * @param courseId            课程id
     * @return
     */
    @PostMapping("/rename")
    public R renameChapterName(@RequestParam("chapterName") String name,
                               @RequestParam("chapterId") int id,
                               @RequestParam("userId") String userId,
                               @RequestParam("courseId") int courseId){
        courseService.renameChapterName(name,id,userId,courseId);
        return R.ok();
    }

    /**
     * 删除指定章节和内容请求
     * @param id               章节id
     * @param type             章节类型
     * @param userId           用户id
     * @param courseId         课程id
     * @return
     */
    @PostMapping("/delete")//删除指定章节和内容
    public R deleteChapterOrContent (@RequestParam("chapterId") int id,
                                     @RequestParam("type") int type,
                                     @RequestParam("userId") String userId,
                                     @RequestParam("courseId") int courseId){

        courseService.deleteContentAndChapter(id,type,userId,courseId);
        return R.ok();
    }

    /**
     * 查询教师列表信息请求
     * @param courseId          课程id
     * @return
     */
    @PostMapping("/teachers")
    public R selectTeacher(@RequestParam("courseId") int courseId) {
        List<User> userList = courseService.selectTeacher(courseId);
        if (userList != null) {
            return R.ok().setData(userList);
        } else {
            return R.error();
        }

    }

    /**
     * 添加教师请求
     * @param dataList             //所选教师列表
     * @param courseId            //课程id
     * @param userId             //用户id
     * @return
     */
    @PostMapping("/addTeacher")
    public R insertTeacher(@RequestParam("dataList") List<String> dataList,
                           @RequestParam("courseId") int courseId,
                           @RequestParam("userId") String userId){
        courseService.addTeachers(dataList,courseId,userId);
        return R.ok("添加教师成功");
    }

    /**
     * 对教师进行模糊查询请求
     * @param searchText                搜索框
     * @param courseId                  课程id
     * @return
     */
    @PostMapping("/searchTeacherNotInCourse")
    public R getTeacherNotInCourseByNameOrNO(@RequestParam("searchText") String searchText,
                                             @RequestParam("courseId") int courseId) {
        List<User> userList = courseService.getTeacherNotInCourseByNameOrNO(searchText, courseId);
        if (userList != null) {
            return R.ok().setData(userList);
        } else {
            return R.error();
        }
    }

    /**
     * 查询不在课程里的部门信息请求
     * @param deptId                    部门id
     * @param courseId                  课程id
     * @return
     */
    @PostMapping("/TeacherNotinCourseByDepartment")
    public R selectTeacherByDepartment(@RequestParam("deptId") int deptId,
                                       @RequestParam("courseId") int courseId) {
        List<User> userList = courseService.selectTeacherByDepartment(deptId, courseId);
        if (userList != null) {
            return R.ok().setData(userList);
        } else {
            return R.error();
        }
    }

    /**
     * 查询课程教师信息请求
     * @param courseId                    课程id
     * @return
     */
    @PostMapping("/selectTeacherByCourse")
    public R selectTeacherByCourse(@RequestParam("courseId") int courseId) {
        List<User> userList = courseService.selectTeacherByCourse(courseId);
        if (userList != null) {
            return R.ok().setData(userList);
        } else {
            return R.error();
        }
    }

    /**
     * 删除教师信息请求
     * @param courseId                      课程id
     * @param teacherId                     教师id
     * @return
     */
    @PostMapping("/deleteTeacher")
    public R deleteCourseParticipantTeacher(@RequestParam("courseId") int courseId,
                                            @RequestParam("teacherId") String teacherId){
        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setCourseid(courseId);
        teacherCourse.setTid(teacherId);
        courseService.deleteCourseParticipantTeacher(courseId,teacherId);
        return R.ok();
    }

    /**
     * 查询教师课程日志请求
     * @param courseId                   课程id
     * @param teacherId                  用户id
     * @return
     */
    @PostMapping("/selectSystemLogByUserIdAndCourseId")
    public R selectSystemLogByUserIdAndCourseId(@RequestParam("courseId") int courseId,
                                        @RequestParam("teacherId") String teacherId){
        List<SystemLogEx> systemLogExList = courseService.selectSystemLogByUserIdAndCourseId(teacherId,courseId);
        if (systemLogExList != null) {
            return R.ok().setData(systemLogExList);
        }else {
            return R.error();
        }
    }

    /**
     * 统计一个班级学生访问该课程的章节内容数数据请求
     * @param classId                    班级id
     * @return
     */

    @PostMapping("/countCourseContent")
    public R countCourseContent(@RequestParam("classId") int classId){
        int courseCount = courseService.countCourseContent(classId);
        if (courseCount > 0) {
            return R.ok().setData(courseCount);
        }else{
            return R.error();
        }
    }

    /**
     * 统计班级每个学生的成绩分布请求
     * @param classId                课程id
     * @return
     */
    @PostMapping("/selectScoreByClassId")
    public R selectScoreByClassId(@RequestParam("classId") int classId){
        List<ExamRecordEx> examRecordExList = courseService.selectScoreByClassId(classId);
        if (examRecordExList != null) {
            return R.ok().setData(examRecordExList);
        }else {
            return R.error();
        }
    }

    /**
     * 获得学生该课程下的访问日志信息
     * @param year              //年份
     * @param mouth             //月份
     * @param classId           //班级id
     * @return
     */
    @PostMapping("/getLogStatistics")
    public R getLogStatistics(@RequestParam("year") int year,
                              @RequestParam("mouth") int mouth,
                              @RequestParam("classId") int classId){
        List<Map<String, Object>> maps = courseService.getLogStatistics(year,mouth,classId);
        if(maps != null){
            return R.ok().setData(maps);
        }else {
            return R.error();
        }
    }
}
