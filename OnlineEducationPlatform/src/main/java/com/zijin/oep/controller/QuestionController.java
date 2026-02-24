package com.zijin.oep.controller;

import com.zijin.oep.model.Question;
import com.zijin.oep.result.R;
import com.zijin.oep.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 题库管理控制器
 */
@RestController
@RequestMapping("/question")//添加题目
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    /**
     * 添加题目请求
     * @param type                     是题目
     * @param level                    题目难度
     * @param content                  题干内容
     * @param answer                   题目答案
     * @param option1                  选项内容1
     * @param option2                  选项内容2
     * @param option3                  选项内容3
     * @param option4                  选项内容4
     * @param folderId                 文件夹id
     * @param createTime               创建时间
     * @param creator                  创建者
     * @param courseId                 课程id
     * @param questionType             题目类型
     * @param userId                   用户id
     * @return
     */
    @PostMapping("/add")
    public R addQuestion(@RequestParam("type") String type,
                             @RequestParam("level") String level,
                             @RequestParam("content") String content,
                             @RequestParam("answer") String answer,
                             @RequestParam("option1") String option1,
                             @RequestParam("option2") String option2,
                             @RequestParam("option3") String option3,
                             @RequestParam("option4") String option4,
                             @RequestParam("folderId") int folderId,
                             @RequestParam("createTime") String createTime,
                             @RequestParam("creator") String creator,
                             @RequestParam("courseId") int courseId,
                             @RequestParam("questionType") int questionType,
                             @RequestParam("userId") String userId){
        Question question = new Question();
        question.setType(type);
        question.setLevel(level);
        question.setContent(content);
        if(questionType==1){//多选题答案需要处理
            String items[] = answer.split(",");
            for(int j=0;j<items.length;j++){
                items[j] = items[j].trim();
            }
            Arrays.sort(items);
            answer = "";
            for(int j=0;j<items.length;j++){
                answer += items[j];
            }
        }
        question.setAnswer(answer);//设置答案
        if(questionType == 0 || questionType == 1){//设置选项的内容
            question.setOption1(option1);
            question.setOption2(option2);
            question.setOption3(option3);
            question.setOption4(option4);
        }
        question.setFolderid(folderId);
        question.setCreatetime(createTime);
        question.setCreator(creator);
        question.setCourseid(courseId);
        question.setQuestiontype(questionType);
        question.setDirty(0);
        int id = questionService.addQuestion(question,userId);
        return R.ok("添加题目成功").setData(id);
    }

    /**
     * 查询题目表中的题目请求
     * @param questionId              题目id
     * @return
     */
    @PostMapping("/select")
    public R selectByQuestionId(@RequestParam("questionId") int questionId){
        Question question = questionService.selectByQuestionId(questionId);
        if(question!=null){
            return R.ok().setData(question);
        }else {
            return R.error();
        }
    }

    /**
     * 通过题目类型筛选请求
     * @param questionType           题目类型
     * @param courseId               课程id
     * @return
     */
    @PostMapping("/selectQuestionType")
    public R seletAllByQuestionType(@RequestParam("questionType") int questionType,
                                    @RequestParam("courseId") int courseId){
        List<Question> questionList = questionService.selectAllByQuestionType(questionType,courseId);
        if(questionList!=null){
            return R.ok().setData(questionList);
        }else {
            return R.error();
        }
    }

    /**
     * 根据题目难度筛选请求
     * @param questionLevel              题目难度
     * @param courseId                   课程id
     * @return
     */
    @PostMapping("selectQuestionLevel")
    public R  selectAllByQuestionLevel(@RequestParam("questionLevel") String questionLevel,
                                       @RequestParam("courseId") int courseId){
        List<Question> questionList = questionService.selectAllByQuestionLevel(questionLevel,courseId);
        if(questionList!=null){
            return R.ok().setData(questionList);
        }else {
            return R.error();
        }
    }

    /**
     * 统计某种题型的数量请求
     * @param questionType              题目类型
     * @param courseId                  课程id
     * @return
     */
    @PostMapping("countQuestionType")
    public R selectCountByQuestionType(@RequestParam("questionType") int questionType,
                                       @RequestParam("courseId") int courseId){
        int count = questionService.selectCountByQuestionType(questionType,courseId);
        return R.ok().setData(count);
    }

    /**
     * 把题目移到回收站请求
     * @param questionId     题目id
     * @param dirty          删除标志
     * @return
     */
    @PostMapping("/updateDirty")
    public R updateDirtyBQuestionId(@RequestParam("questionId") int questionId,
                                     @RequestParam("dirty") int dirty){
        questionService.updateDirtyById(questionId,dirty);
        return R.ok();
    }

    /**
     * 把题目从一个文件夹移到另一个文件夹请求
     * @param folderId          文件夹id
     * @param id                文件夹父id
     * @param userId            用户id
     * @param courseId          课程id
     * @return
     */
    @PostMapping("/questionMove")
    public R moveQuestionToFolder(@RequestParam("folderId") int folderId,
                                  @RequestParam("id") int id,
                                  @RequestParam("userId") String userId,
                                  @RequestParam("courseId") int courseId){
        questionService.moveQuestionToFolder(folderId,id,userId,courseId);
        return R.ok();
    }

    /**
     * 复制某道题目请求
     * @param questionId            题目id
     * @param userId                用户id
     * @param courseId              课程id
     * @return
     */
    @PostMapping("/copy")
    public R copyById(@RequestParam("questionId") int questionId,
                      @RequestParam("userId") String userId,
                      @RequestParam("courseId") int courseId){
        questionService.copyById(questionId,userId,courseId);
        return R.ok();
    }

    /**
     * 更新题目信息
     * @param option1                  选项内容1
     * @param option2                  选项内容2
     * @param option3                  选项内容3
     * @param option4                  选项内容4
     * @param stemContent              题目题干
     * @param answerContent            答案信息
     * @param questionId               题目id
     * @param userId                   用户id
     * @param courseId                 课程id
     * @return
     */
    @PostMapping("/updateQuestion")//编辑某道题目
    public R updateQuestionContentById(@RequestParam("option1") String option1,
                                       @RequestParam("option2") String option2,
                                       @RequestParam("option3") String option3,
                                       @RequestParam("option4") String option4,
                                       @RequestParam("stemContent") String stemContent,
                                       @RequestParam("answerContent") String answerContent,
                                       @RequestParam("questionId") int questionId,
                                       @RequestParam("userId") String userId,
                                       @RequestParam("courseId") int courseId){
        questionService.updateQuestionContentById(option1,option2, option3,option4,stemContent,answerContent,questionId,userId,courseId);
        return R.ok();
    }

}
