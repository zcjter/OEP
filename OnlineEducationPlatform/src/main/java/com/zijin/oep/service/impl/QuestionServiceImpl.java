package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.FolderMapper;
import com.zijin.oep.mapper.QuestionMapper;
import com.zijin.oep.mapper.SystemLogMapper;
import com.zijin.oep.model.Folder;
import com.zijin.oep.model.Question;
import com.zijin.oep.model.SystemLog;
import com.zijin.oep.service.QuestionService;
import com.zijin.oep.utils.TreeNode1;
import com.zijin.oep.utils.UtilTools;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionServiceImpl implements QuestionService{

    private QuestionMapper questionMapper;//question表mapper
    private FolderMapper folderMapper;//folder表mapper
    private SystemLogMapper systemLogMapper;//systemlog表mapper

    public QuestionServiceImpl(QuestionMapper questionMapper, FolderMapper folderMapper,
                               SystemLogMapper systemLogMapper){
        this.questionMapper = questionMapper;
        this.folderMapper = folderMapper;
        this.systemLogMapper = systemLogMapper;
    }

    /**
     * 添加课程信息
     * @param question
     * @param userId
     * @return
     */
    @Override
    public int addQuestion(Question question,String userId){
        questionMapper.insert(question);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(question.getCourseid());
        systemLog.setDescr("创建一道"+changeQuestionNumberToName(question.getQuestiontype()));
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);

        return questionMapper.getMaxId().intValue();
    }

    /**
     * 查询文件夹下的题目信息
     * @param folderId
     * @param courseId
     * @return
     */
    @Override
    public List<Question> selectAllByFolderId(int folderId, int courseId){
        return  questionMapper.selectAllByFolderId(folderId,courseId);
    }

    /**
     * 查询一道题目
     * @param questionId
     * @return
     */
    @Override
    public Question selectByQuestionId(int questionId){
        return questionMapper.selectById(questionId);
    }

    /**
     * 按所以题目类型筛选
     * @param questionType
     * @param courseId
     * @return
     */
    @Override
    public  List<Question> selectAllByQuestionType(int questionType,int courseId){
        return questionMapper.selectAllByQuestionType(questionType,courseId);
    }

    /**
     * 按所有题目难度筛选
     * @param questionLevel
     * @param courseId
     * @return
     */
    @Override
    public List<Question> selectAllByQuestionLevel(String questionLevel,int courseId){
        return questionMapper.selectAllByQuestionLevel(questionLevel,courseId);
    }

    /**
     * 统计某种题目类型的数量
     * @param questionType
     * @param courseId
     * @return
     */
    @Override
    public int selectCountByQuestionType(int questionType,int courseId){
        return questionMapper.selectCountByQuestionType(questionType,courseId);
    }

    /**
     * 按照题目内容模糊查询
     * @param searchText
     * @param courseId
     * @return
     */
    @Override
    public List<Question> getQuestionContent(String searchText, int courseId){
        return questionMapper.getQuestionContent(searchText, courseId);
    }

    /**
     * 删除文件夹下的题目信息
     * @param folderId
     * @param dirty
     */
    @Override
    public void updateDirtyByFolderId(int folderId, int dirty){
        questionMapper.updateDirtyByFolderId(folderId,dirty);
    }

    /**
     * 删除一道题目
     * @param questionId
     * @param dirty
     */
    @Override
    public void updateDirtyById(int questionId, int dirty){
        questionMapper.updateDirtyById(questionId,dirty);
    }

    /**
     * 移动某道题到文件夹
     * @param folderId
     * @param id
     * @param userId
     * @param courseId
     */
    @Override
    public void moveQuestionToFolder(int folderId, int id,String userId,int courseId){
        questionMapper.moveQuestionToFolder(folderId, id);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setDescr("移动一道题目");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 复制题目
     * @param questionId
     * @param userId
     * @param courseId
     */
    @Override
    public void copyById(int questionId,String userId,int courseId){
        Question question = questionMapper.selectById(questionId);
        questionMapper.insertQuestion(question);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("复制一道题目");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 更新题目内容
     * @param option1
     * @param option2
     * @param option3
     * @param option4
     * @param stemContent
     * @param answerContent
     * @param questionId
     * @param userId
     * @param courseId
     */
    @Override
    public void updateQuestionContentById(String option1,String option2,String option3,String option4,String stemContent,String answerContent,int questionId,String userId,int courseId){
        questionMapper.updateQuestionContentById(option1,option2,option3,option4,stemContent,answerContent, questionId);
        Question question = questionMapper.selectById(questionId);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("编辑一道"+changeQuestionNumberToName(question.getQuestiontype()));
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //私有方法
    /**
     * 将类型转为汉字
     * @param questionType
     * @return
     */
    private String changeQuestionNumberToName(int questionType){
        switch (questionType) {
            case 0:
                return "单选题";
            case 1:
                return "多选题";
            case 2:
                return "判断题";
            case 3:
                return "填空题";
            case 4:
                return "简答题";
            default:
                return "未知题型"; // 添加默认返回值，处理未匹配到任何情况的情况
        }
    }
}
