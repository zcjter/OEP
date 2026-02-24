package com.zijin.oep.service;

import com.zijin.oep.model.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ExamService {
    Boolean createExam(Map<String, Object> requestData);
    Boolean submitExamPaper(Map<String, Object> requestData);
    List<ExamPaper> selectAllByFolderId(int courseId, int parentId);
    List<Question> selectAllQuestion(int examPaperId, int number);
    List<ExamQuestionType> countAllByQuestionType(int examPaperId, int number);
    List<PaperQuestion> selectAllNumExamPaper(int paperId, int number);
    ExamPaper selectByExamId(int examPaperId);
    void sealExamPaper(int examPaperId,String password,String userId,int courseId);
    void unsealExamPaper(int examPaperId,String password,String userId,int courseId);
    boolean releaseExamPaper(@NotNull Map<String, Object> requestData);
    List<ExamPaperReleaseEx> selectExamPaperByCourseIdAndStudentId(int courseId, String studentId);
    List<QuestionEx1> getExamPaper(int paperId, ExamRecord examRecord,String userId,int courseId);
    ExamRecord selectExamPaperRecordByPaperId(int paperId,String studentId);
    List<ExamPaperReleaseEx1> selectAllByClassId(int classId);
    List<ExamRecord>selectExamPaperByExamId(int paperId,int classId);
    int countStudentsByClassId(int classId);
    List<QuestionEx2> selectByStudentIdAndQuestionId(int paperId, int number,String studentId);
    boolean teacherRating(@NotNull Map<String, Object> requestData);
}
