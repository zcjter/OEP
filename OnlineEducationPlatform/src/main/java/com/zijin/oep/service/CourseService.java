package com.zijin.oep.service;

import com.zijin.oep.model.*;
import com.zijin.oep.utils.TreeNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseService {

    boolean createCourse(MultipartFile coverFile,Course course,String teaId);
    void insertChapter(Chapter chapter,String userId);
    void insertContent( int chapterId,Content contents,String userId);
    void updateContent(int chapterId,String content,String userId,int courseId);
    void adjustChapter(int parentId, int id,String userId,int courseId);
    void renameChapterName(String name, int id,String userId,int courseId);
    void deleteContentAndChapter(int chapterId,int type,String userId,int courseId);
    void addTeachers( List<String> dataList,int courseId,String userId);
    void deleteCourseParticipantTeacher(int courseId,String teacherId);

    List<Course> fetchCourseList(String uid);
    List<Course> fetchParticipateCourseList(String uid);
    List<TreeNode> fetchChapter(int courseId);
    Content fetchChapterContent(int chapterId, int courseId,int classId,String userId,int role);
    Content fetchChapterTeacherContent(int chapterId,int courseId);
    List<User> selectTeacher(int courseId);
    List<User> getTeacherNotInCourseByNameOrNO(String searchText,int courseId);
    List<User> selectTeacherByDepartment(int pid,int courseId);
    List<User> selectTeacherByCourse(int courseId);
    List<CourseEx> fetchStudentCourseList(String studentId);
    List<SystemLogEx> selectSystemLogByUserIdAndCourseId(String teacherId,int courseId);
    int countCourseContent(int classId);
    List<ExamRecordEx> selectScoreByClassId(int classId);
    List<Map<String, Object>> getLogStatistics(int year, int month, int classId);
}
