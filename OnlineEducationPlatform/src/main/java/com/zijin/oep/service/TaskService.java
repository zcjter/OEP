package com.zijin.oep.service;


import com.zijin.oep.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface TaskService {
    boolean saveTask(@NotNull Map<String, Object> requestData);
    List<Task> selectByCourseId(int courseId, int parentId);
    List<QuestionEx3> selectQuestionByTaskId(int taskId);
    boolean releaseTask(@NotNull Map<String, Object> requestData);
    List<TaskReleaseEx> selectClassTask(int classId);
    List<TaskReleaseEx1> selectStudentTask(int courseId, String studentId);
    boolean submitTask(@NotNull Map<String, Object> requestData);
    List<TaskRecord> selectByTaskIdAndClassId(int taskId, int classId);
    Map<String, Integer> countTaskStatus(int taskId, int classId);
    List<QuestionEx4> selectTaskByTaskIdAndStudentId(int taskId, String studentId);
    boolean teacherRating(@NotNull Map<String, Object> requestData);
    List<TaskRecord> selectStudentNotInTask(int classId,int taskId);
}
