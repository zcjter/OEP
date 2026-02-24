package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    void insertQuestion(Question question);
    List<Question> selectAllByFolderId(int folderId, int courseId);
    List<Question> selectAllByQuestionType(int questionType,int courseId);
    List<Question> selectAllByQuestionLevel(String questionLevel,int courseId);
    List<Question> selectRandomQuestionsByQuestionType(int questionType,int courseId,int count);
    List<Question> selectRandomQuestionsByFolderId(int questionType,int courseId,int folderId,int count);
    int selectCountByQuestionType(int questionType,int courseId);
    List<Question> getQuestionContent(String searchText, int courseId);
    List<Question> selectAllQuestion(int examPaperId, int number);
    List<ExamQuestionType> countAllByQuestionType(int examPaperId, int number);
    void updateDirtyByFolderId(int folderId, int dirty);
    void updateDirtyById(int questionId, int dirty);
    void moveQuestionToFolder(int folderId, int id);
    void updateQuestionContentById(String option1,String option2,String option3,String option4,String stemContent,String answerContent,int questionId);
    Integer selectRandomNumByPaperId(int paperId);
    List<QuestionEx1> selectQuestionsByRandomNum(int paperId, int num);
    List<QuestionEx2> selectByStudentIdAndQuestionId(int paperId, int number,String studentId);
    List<QuestionEx3> selectQuestionByTaskId(int taskId);
    Integer getMaxId();
    List<QuestionEx4> selectByTaskIdAndStudentId(int taskId, String studentId);
}
