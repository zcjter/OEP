package com.zijin.oep.controller;

import com.zijin.oep.model.*;
import com.zijin.oep.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.zijin.oep.result.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 考试管理控制器
 */
@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    /**
     * 创建试卷请求
     * @param requestData
     * @return
     */
    @PostMapping("/create")
    public R createExamPaper(@RequestBody Map<String, Object> requestData){
        Boolean ret =  examService.createExam(requestData);
        if(ret==true)
           return R.ok();
        else
            return R.error();
    }

    /**
     * 提交试卷请求
     * @param requestData
     * @return
     */
    @PostMapping("/submitExamPaper")
    public R submitExamPaper(@RequestBody Map<String, Object> requestData){
        Boolean ret = examService.submitExamPaper(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();
    }
    /**
     * 获取指定试卷请求
     * @param examPaperId           试卷id
     * @return
     */
    @PostMapping("selectByexamId")
    public R selectAllByExamId(@RequestParam("examPaperId") int examPaperId){
        ExamPaper examPaper = examService.selectByExamId(examPaperId);
        if(examPaper!=null){
            return R.ok().setData(examPaper);
        }else{
            return R.error();
        }
    }

    /**
     * 获取指定试卷题目列表请求
     * @param examPaperId               试卷id
     * @param number                    试卷份数随机数
     * @return
     */
    @PostMapping("/selectAllQuestion")
    public R selectAllQuestion(@RequestParam("examPaperId") int examPaperId,
                               @RequestParam("number") int number){
        List<Question> questionList = examService.selectAllQuestion(examPaperId,number);
        if(questionList!=null){
            return R.ok().setData(questionList);
        }else{
            return R.error();
        }
    }

    /**
     * 获取指定试卷题型数量统计信息请求
     * @param examPaperId                   //试卷id
     * @param number                        //试卷份数
     * @return
     */
    @PostMapping("/countAllByQuestionType")
    public R countAllByQuestionType(@RequestParam("examPaperId") int examPaperId,
                                    @RequestParam("number") int number){
        List<ExamQuestionType> examQuestionTypeList = examService.countAllByQuestionType(examPaperId,number);
        if(examQuestionTypeList !=null){
            return R.ok().setData(examQuestionTypeList);
        }else{
            return R.error();
        }
    }

    /**
     * 查询试卷和题目关联表请求
     * @param paperId        试卷id
     * @param number         试卷随机数
     * @return
     */
    @PostMapping("/selectAllNumExamPaper")
    public R selectAllNumExamPaper(@RequestParam("examPaperId") int paperId,
                                   @RequestParam("number") int number){
        List<PaperQuestion> paperQuestionList = examService.selectAllNumExamPaper(paperId,number);
        if(paperQuestionList!=null){
            return R.ok().setData(paperQuestionList);
        }else{
            return R.error();
        }
    }
    /**
     * 试卷封存请求
     * @param examPaperId           试卷id
     * @param password              封存密码
     * @return
     */
    @PostMapping("/seal")
    public R sealExamPaper(@RequestParam("examPaperId") int examPaperId,
                           @RequestParam("accessPassword") String password,
                           @RequestParam("userId") String userId,
                           @RequestParam("courseId") int courseId){
        examService.sealExamPaper(examPaperId,password,userId,courseId);
        return R.ok();
    }

    /**
     * 试卷解封请求
     * @param examPaperId
     * @param password
     * @return
     */
    @PostMapping("/unseal")
    public R unsealExamPaper(@RequestParam("examPaperId") int examPaperId,
                             @RequestParam("accessPassword") String password,
                             @RequestParam("userId") String userId,
                             @RequestParam("courseId") int courseId){
        examService.unsealExamPaper(examPaperId,password,userId,courseId);
        return R.ok();
    }

    /**
     * 发布试卷请求
     * @param requestData
     * @return
     */
    @PostMapping("/release")
    public R releaseExamPaper(@RequestBody Map<String, Object> requestData){
        boolean ret = examService.releaseExamPaper(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();
    }

    /**
     * 学生进入课程后点击考试获取试卷信息请求
     * @param courseId             课程id
     * @param studentId            学生id
     * @return
     */
    @PostMapping("selectExamPaper")
    public R selectByClassId(@RequestParam("courseId") int courseId,
                             @RequestParam("studentId") String studentId){
        List<ExamPaperReleaseEx> examPaperStatusExList = examService.selectExamPaperByCourseIdAndStudentId(courseId, studentId);
        if(examPaperStatusExList!=null){
            return R.ok().setData(examPaperStatusExList);
        }else{
            return R.error();
        }
    }
    /**
     * 学生进入考试随机获取试卷请求
     * @param studentId     学生id
     * @param studentName   学生姓名
     * @param enterTime     进入考试时间
     * @param paperId       试卷id
     * @param userId        用户id
     * @param courseId      课程id
     * @return
     */
    @PostMapping("/getExamPaper")
    public R getExamPaper(@RequestParam("studentId") String studentId,
                          @RequestParam("studentName") String studentName,
                          @RequestParam("enterTime") Timestamp enterTime,
                          @RequestParam("paperId") int paperId,
                          @RequestParam("classId") int classId,
                          @RequestParam("userId") String userId,
                          @RequestParam("courseId") int courseId){
        ExamRecord examRecord = new ExamRecord();
        examRecord.setSid(studentId);
        examRecord.setSname(studentName);
        examRecord.setStatus("进行中");
        examRecord.setPaperid(paperId);
        examRecord.setClassid(classId);
        // 获取北京时区
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        // 设置时区
        enterTime.setTime(enterTime.getTime()+timeZone.getRawOffset());
        examRecord.setStarttime(enterTime);

        List<QuestionEx1> questionEx1s = examService.getExamPaper(paperId,examRecord,userId,courseId);
        if(questionEx1s!=null){
            return R.ok().setData(questionEx1s);
        }else{
            return R.error();
        }
    }

    /**
     * 学生提交试卷成功请求记录
     * @param paperId               试卷id
     * @param studentId             学生id
     * @return
     */
    @PostMapping("/submitSucess")
    public R selectExamPaperRecordByPaperId(@RequestParam("paperId") int paperId,
                                            @RequestParam("studentId") String studentId){
        ExamRecord  examRecord = examService.selectExamPaperRecordByPaperId(paperId,studentId);
        if(examRecord!=null){
            return R.ok().setData(examRecord);
        }else{
            return R.error();
        }
    }

    /**
     * 统计在班级里的学生请求
     * @param classId          班级id
     * @return
     */
    @PostMapping("/selectAllByClassId")
    public R selectAllByClassId(@RequestParam("classId") int classId){
        List<ExamPaperReleaseEx1> examPaperReleaseEx1List = examService.selectAllByClassId(classId);
        if(examPaperReleaseEx1List!=null){
            return R.ok().setData(examPaperReleaseEx1List);
        }else{
            return R.error();
        }
    }

    /**
     * 统计tb_examrecord学生的考试记录请求
     * @param paperId        试卷id
     * @param classId        班级id
     * @return
     */
    @PostMapping("/selectByExamId")
    public R selectByStudentId(@RequestParam("paperId") int paperId,
                               @RequestParam("classId") int classId){
        List<ExamRecord> examRecordList = examService.selectExamPaperByExamId(paperId,classId);
        if(examRecordList!=null){
            return R.ok().setData(examRecordList);
        }else{
            return R.error();
        }
    }
    @PostMapping("/countStudentsByClassId")
    public R countStudentsByClassId(@RequestParam("classId") int classId){
        int studentCount = examService.countStudentsByClassId(classId);
        return R.ok().setData(studentCount);
    }

    /**
     * 查询某学生题目信息的请求
     * @param paperId              试卷id
     * @param number               随机试卷份数
     * @param studentId            学生id
     * @return
     */

    @PostMapping("/selectByStudentIdAndQuestionId")
    public R selectByStudentIdAndQuestionId(@RequestParam("examPaperId") int paperId,
                                            @RequestParam("number") int number,
                                            @RequestParam("studentId") String studentId){
        List<QuestionEx2> questionEx2List = examService.selectByStudentIdAndQuestionId(paperId,number,studentId);
        if(questionEx2List!=null){
            return R.ok().setData(questionEx2List);
        }else{
            return R.error();
        }
    }

    /**
     * 老师批改主观题请求
     * @param requestData
     * @return
     */
    @PostMapping("/updateReviewRecord")
    public R updateReviewRecord(@RequestBody Map<String, Object> requestData){
        boolean ret = examService.teacherRating( requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();

    }
}
