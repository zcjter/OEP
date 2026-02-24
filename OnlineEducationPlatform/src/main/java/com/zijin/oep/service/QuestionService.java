package com.zijin.oep.service;

import com.zijin.oep.model.Folder;
import com.zijin.oep.model.Question;
import com.zijin.oep.utils.TreeNode1;

import java.util.List;

public interface QuestionService {
    int addQuestion(Question question,String userId);
    List<Question> selectAllByFolderId(int folderId, int courseId);
    List<Question> selectAllByQuestionType(int questionType,int courseId);
    List<Question> selectAllByQuestionLevel(String questionLevel,int courseId);
    List<Question> getQuestionContent(String searchText, int courseId);
    int selectCountByQuestionType(int questionType,int courseId);
    Question selectByQuestionId(int questionId);
    void updateDirtyByFolderId(int folderId, int dirty);
    void updateDirtyById(int questionId, int dirty);

    void moveQuestionToFolder(int folderId, int id,String userId,int courseId);

    void copyById(int questionId,String userId,int courseId);
    void updateQuestionContentById(String option1,String option2,String option3,String option4,String stemContent,String answerContent,int questionId,String userId,int courseId);
}
