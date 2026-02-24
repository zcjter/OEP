package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.*;
import com.zijin.oep.model.*;
import com.zijin.oep.request.FolderInfo;
import com.zijin.oep.request.QuestionInfo;
import com.zijin.oep.service.ExamService;
import com.zijin.oep.utils.UtilTools;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ExamServiceImpl implements ExamService {
    private ExamPaperMapper examPaperMapper;//试卷表mapper
    private PaperQuestionMapper paperQuestionMapper;//试卷题目关联表mapper
    private QuestionMapper questionMapper;//题目表mapper
    private StudentClassMapper studentClassMapper;//学生班级关联表mapper
    private ExamRecordMapper examRecordMapper;//试卷记录表mapper
    private AnswerRecordMapper answerRecordMapper;//学生试卷答题答案记录表mapper
    private ExamPaperReleaseMapper examPaperReleaseMapper;//试卷发布表mapper
    private CourseClassMapper courseClassMapper;//课程班级关联表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper
    private CourseLogMapper courseLogMapper;//课程日志表mapper
    private UserMapper userMapper;//用户表mapper

    private ExamPaper examPaper = null;
    private List<AnswerRecord> answerRecordList = null;
    private List<QuestionInfo> questionInfoList = null;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public ExamServiceImpl(ExamPaperMapper examPaperMapper,PaperQuestionMapper paperQuestionMapper,
                           QuestionMapper questionMapper,
                           StudentClassMapper studentClassMapper,AnswerRecordMapper answerRecordMapper,
                           ExamRecordMapper examRecordMapper, ExamPaperReleaseMapper examPaperReleaseMapper,
                           CourseClassMapper courseClassMapper,SystemLogMapper systemLogMapper,
                           CourseLogMapper courseLogMapper,UserMapper userMapper){
        this.examPaperMapper = examPaperMapper;
        this.paperQuestionMapper = paperQuestionMapper;
        this.questionMapper = questionMapper;
        this.studentClassMapper = studentClassMapper;
        this.answerRecordMapper = answerRecordMapper;
        this.examRecordMapper = examRecordMapper;
        this.examPaperReleaseMapper = examPaperReleaseMapper;
        this.courseClassMapper = courseClassMapper;
        this.systemLogMapper = systemLogMapper;
        this.courseLogMapper = courseLogMapper;
        this.userMapper = userMapper;
    }

    /**
     * 自动创建一张试卷
     * @param requestData
     * @return
     */
    @Override
    public Boolean createExam(Map<String, Object> requestData){//创建试卷
        //step1 获取请求数据
        getRequestData(requestData);
        //step2 插入tb_exampaper表
        examPaper.setSealflag(0);//新建试卷初始为解封状态
        examPaperMapper.insert(examPaper);
        String userId = (String) requestData.get("userId");//获取userId
        int courseId = (Integer) requestData.get("courseId");

        int examId = examPaperMapper.getMaxId();
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        ExamPaper examPaper = examPaperMapper.selectById(examId);
        systemLog.setDescr("创建"+examPaper.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
        //step3 插入tb_paperquestion
        for(int index=0;index<examPaper.getCount();index++){
            for(int i=0;i<questionInfoList.size();i++)
                selectExamQuestion(questionInfoList.get(i),examId,index+1);
        }
        return true;
    }

    /**
     *学生提交试卷
     * @param requestData
     * @return
     */
    @Override
    public Boolean submitExamPaper(@NotNull Map<String, Object> requestData){
        ArrayList<Map<String, Object>> AnswerData = ( ArrayList<Map<String, Object>>) requestData.get("answerList");
        int paperId = (Integer) requestData.get("paperId");
        String studentId = (String) requestData.get("studentId");
        AnswerRecord answerRecord = new AnswerRecord();
        answerRecord.setSid(studentId);
        answerRecord.setPaperid(paperId);
        answerRecord.setScore(0);
        for(int i=0;i< AnswerData.size();i++){
            int questionId =(Integer)AnswerData.get(i).get("questionId");
            int questionType = (Integer)AnswerData.get(i).get("questionType");
            String answer="";
            if(questionType!=1){
                answer = (String)AnswerData.get(i).get("answerText");
            }
            else{
                ArrayList<String> L = (ArrayList<String>)AnswerData.get(i).get("answerText");
                answer = L.toString();
                answer = answer.substring(1,answer.length()-1);
                //多选题答案需要去掉逗号按字母顺序存储
                String items[] = answer.split(",");//拆分
                for(int j=0;j<items.length;j++){
                    items[j] = items[j].trim();//去空白
                }
                Arrays.sort(items);//排序
                answer = "";
                for(int j=0;j<items.length;j++){
                    answer += items[j];//重新拼接答案字符串
                }
            }

            answerRecord.setQuestionid(questionId);
            answerRecord.setQuestiontype(questionType);
            answerRecord.setAnswer(answer);
            //step1 插入某道题的答题记录
            answerRecordMapper.insert(answerRecord);
            //step2 客观题自动评卷
            if(questionType!=4&&questionType!=3)
                answerRecordMapper.automaticScoring(questionType,studentId);
        }//循环结束
        //step3更新整张试卷记录
        // 获取前端传递过来的时间戳字符串
        Timestamp currentTime = UtilTools.timeString2TimeStamp((String)requestData.get("currentTime"));
        //获取客观题正确率
        float objectiveaccuracy = calcObjectiveAccuary(paperId,studentId);
        // 然后可以使用 currentTime 进行操作
        examRecordMapper.updateExamRecord(currentTime,paperId,studentId,objectiveaccuracy);
        int classId = (Integer) requestData.get("classId");
        int courseId  = (Integer)requestData.get("courseId");

        ExamPaper examPaper = examPaperMapper.selectById(paperId);
        User user = userMapper.selectById(studentId);

        CourseLog courseLog = new CourseLog();
        courseLog.setUserid(studentId);
        courseLog.setCourseid(courseId);
        courseLog.setDate(UtilTools.getCurrentTime());
        courseLog.setClassid(classId);
        courseLog.setDescr(user.getName()+"提交"+examPaper.getName());

        //往tb_courselog插入数据
        courseLogMapper.insert(courseLog);
        return true;
    }

    /**
     * 查询试卷库的试卷列表
     * @param courseId
     * @param parentId
     * @return
     */
    @Override
    public  List<ExamPaper> selectAllByFolderId(int courseId,int parentId){
        return examPaperMapper.selectAllByFolderId(courseId,parentId);
    }

    /**
     * 实现查询某张试卷的所有份数题目预览
     * @param examPaperId
     * @return
     */
    @Override
    public  ExamPaper selectByExamId(int examPaperId){
        return examPaperMapper.selectById(examPaperId);
    }

    /**
     * 查询题目表中题目信息
     * @param examPaperId
     * @param number
     * @return
     */
    @Override
    public  List<Question> selectAllQuestion(int examPaperId,int number){
        return questionMapper.selectAllQuestion(examPaperId,number);
    }

    /**
     * 统计某种题型题目数量
     * @param examPaperId
     * @param number
     * @return
     */
    @Override
    public List<ExamQuestionType> countAllByQuestionType(int examPaperId, int number){
        return questionMapper.countAllByQuestionType(examPaperId,number);
    }

    /**
     * 查询题目与试卷关联表所有信息
     * @param paperId
     * @param number
     * @return
     */
    @Override
    public List<PaperQuestion> selectAllNumExamPaper(int paperId,int number){
        return paperQuestionMapper.selectAllNumExamPaper(paperId,number);
    }
    /**
     * 封存一张试卷
     * @param examPaperId
     * @param password
     */
    @Override
    public  void sealExamPaper(int examPaperId,String password,String userId,int courseId){
            String password1 = passwordEncoder.encode(password);
            examPaperMapper.sealExamPaper(examPaperId,password1);
            ExamPaper examPaper = examPaperMapper.selectById(examPaperId);
            SystemLog systemLog = new SystemLog();
            systemLog.setUserid(userId);
            systemLog.setDate(UtilTools.getCurrentTime());
            systemLog.setDescr(examPaper.getName());
            systemLog.setDescr("封存试卷"+examPaper.getName());
            //往tb_systemlog插入日志
            systemLogMapper.insert(systemLog);
    }

    /**
     * 解封一张试卷
     * @param examPaperId
     */
    @Override
    public void unsealExamPaper(int examPaperId,String password,String userId,int courseId){
        ExamPaper examPaper = examPaperMapper.selectById(examPaperId);
        if(passwordEncoder.matches(password,examPaper.getPassword())){
            examPaperMapper.unsealExamPaper(examPaperId);
        }
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setDescr(examPaper.getName());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("解封"+examPaper.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 试卷发布服务
     * @param requestData
     */
    @Override
    public boolean releaseExamPaper(@NotNull Map<String, Object> requestData){
        //往tb_examrelease表插入试卷发布记录(多个班级)
        ExamPaperRelease examPaperRelease = new ExamPaperRelease() ;
        examPaperRelease.setPaperid((Integer)requestData.get("paperId"));
        examPaperRelease.setStarttime(UtilTools.timeString2TimeStamp((String)requestData.get("startTime")));
        examPaperRelease.setEndtime(UtilTools.timeString2TimeStamp((String)requestData.get("endTime")));
        String userid = (String)requestData.get("userId");

        ArrayList<Object> classData = ( ArrayList<Object>) requestData.get("classIds");
        for(int i=0;i<classData.size();i++) {
            int classId = (int) classData.get(i);
            examPaperRelease.setClassid(classId);
            examPaperReleaseMapper.insert(examPaperRelease);//插入班级试卷发布记录
        }
        int paperId = (Integer)requestData.get("paperId");
        int courseId = (Integer) requestData.get("courseId");
        ExamPaper examPaper = examPaperMapper.selectById(paperId);

        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userid);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("发布"+examPaper.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
        return true;
    }
    /**
     *统计试卷信息
     * @param courseId
     * @param studentId
     * @return
     */
    @Override
    public  List<ExamPaperReleaseEx> selectExamPaperByCourseIdAndStudentId(int courseId, String studentId){
        //step1 根据学生id和课程id获取学生所在课程班级信息
        CourseClass courseClass = courseClassMapper.selectCourseClass(studentId,courseId);
        //step2 根据班级id获取试卷信息
        List<ExamPaperReleaseEx> examPaperReleaseExList =
                examPaperReleaseMapper.selectExamPaperByClassId(courseClass.getId());
        //step3 根据studentId和paperId查询tb_examrecord的状态字段
        for(int i=0;i<examPaperReleaseExList.size();i++){
            int paperId = examPaperReleaseExList.get(i).getPaperid();
            ExamRecord examRecord = examRecordMapper.selectByPaperIdAndStuId(paperId,studentId);
            if(examRecord!=null)
                examPaperReleaseExList.get(i).setStatus(examRecord.getStatus());
            else
                examPaperReleaseExList.get(i).setStatus("待做");
        }
        return examPaperReleaseExList;
    }
    /**
     * 随机获取某份试卷id下的试题信息,并在tb_examrecord插入数据
     * @param paperId
     * @return
     */
    @Override
    public List<QuestionEx1> getExamPaper(int paperId, ExamRecord examRecord,String userId,int courseId){
        //step1 随机获取当前试卷的某份序号
        int num = questionMapper.selectRandomNumByPaperId(paperId);
        examRecord.setCount(num);
        //step2 在tb_examrecord中插入记录
        examRecordMapper.insert(examRecord);

        ExamPaper examPaper = examPaperMapper.selectById(paperId);
        User user = userMapper.selectById(userId);

        CourseLog courseLog = new CourseLog();
        courseLog.setUserid(userId);
        courseLog.setCourseid(courseId);
        courseLog.setDate(UtilTools.getCurrentTime());
        courseLog.setClassid(examRecord.getClassid());
        courseLog.setDescr(user.getName()+"进入"+examPaper.getName());

        //往tb_courselog插入数据
        courseLogMapper.insert(courseLog);
        //step3 获取某份序号试卷的题目列表
        return questionMapper.selectQuestionsByRandomNum(paperId,num);
    }
    /**
     * 查询某场考试的考试记录
     * @param paperId
     * @param studentId
     * @return
     */
    @Override
    public ExamRecord selectExamPaperRecordByPaperId(int paperId,String studentId){
        return examRecordMapper.selectByPaperIdAndStuId(paperId,studentId);
    }

    /**
     * 查询某个班级发布的考试信息
     * @param classId
     * @return
     */
    @Override
    public List<ExamPaperReleaseEx1> selectAllByClassId(int classId){

        return examPaperReleaseMapper.selectAllByClassId(classId) ;
    }

    /**
     * 统计tb_examrecord学生的考试记录
     * @param paperId
     * @return
     */
    @Override
    public List<ExamRecord> selectExamPaperByExamId(int paperId,int classId){
        return examRecordMapper.selectExamPaperByExamId(paperId,classId);
    }

    /**
     * 统计班级总人数
     * @param classId
     * @return
     */
    @Override
    public int countStudentsByClassId(int classId){
        return studentClassMapper.countStudentsByClassId(classId);
    }

    /**
     * 查询学生某张试卷题目信息
     * @param paperId
     * @param number
     * @param studentId
     * @return
     */
    @Override
    public List<QuestionEx2> selectByStudentIdAndQuestionId(int paperId, int number,String studentId){
        return questionMapper.selectByStudentIdAndQuestionId(paperId,number,studentId);
    }

    /**
     * 教师批改主观题
     * @param requestData
     * @return
     */
    @Override
    public boolean teacherRating(@NotNull Map<String, Object> requestData) {
        ArrayList<Map<String, Object>> ExamData = ( ArrayList<Map<String, Object>>) requestData.get("subjectQuestion");
        int paperId = (Integer)requestData.get("paperId");
        String studentId = (String) requestData.get("studentId");

        for(int i=0;i<ExamData.size();i++){
            int questionId = (int)ExamData.get(i).get("questionId");
            int score = (int) ExamData.get(i).get("score");
            answerRecordMapper.reviewSubjectiveQuestions(score,studentId,paperId,questionId);
        }

        // 获取前端传递过来的时间戳字符串
        Timestamp reviewTime = UtilTools.timeString2TimeStamp((String)requestData.get("reviewTime"));

        String reviewer = (String) requestData.get("reviewer");
        String teacherId = (String) requestData.get("teacherId");


        ExamRecord examRecord = new ExamRecord();
        examRecord.setReviewer(reviewer);
        examRecordMapper.updateReviewRecord(reviewer,reviewTime,paperId,studentId);

        ExamPaper examPaper = examPaperMapper.selectById(paperId);
        User user = userMapper.selectById(studentId);
        int courseId = (Integer)requestData.get("courseId");

        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(teacherId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("批改"+user.getName()+examPaper.getName()+"主观题");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
        return true;
    }
    /**
     * 把随机取的单道题的数据插入数据库，并且随机生成多份试卷
     * @param questionInfo
     * @param examId
     * @param index
     */

    private void selectExamQuestion(QuestionInfo questionInfo,int examId,int index){

        if(questionInfo.getMethod() == 0){//处理从题库抽题的情况
            List<Question> questionList;
            int count = questionInfo.getCount();//获得从题库抽题的数量
            if(count>0){
                questionList = questionMapper.selectRandomQuestionsByQuestionType(questionInfo.getType(),examPaper.getCourseid(),count);
                int score = questionInfo.getScore() / questionInfo.getCount();
                for(int i=0;i<questionList.size();i++){
                    Question question = questionList.get(i);
                    PaperQuestion paperQuestion = new PaperQuestion();
                    paperQuestion.setQuestionid(question.getId());
                    paperQuestion.setScore(score);
                    paperQuestion.setPaperid(examId);
                    paperQuestion.setNum(index);
                    paperQuestionMapper.insert(paperQuestion);
                }
            }
        }else if(questionInfo.getMethod() == 1){
                List<Question> questionAll = new ArrayList<>();
            for (FolderInfo folderInfo : questionInfo.getFolderInfoList()){
                List<Question> questionList;
                int count = folderInfo.getCount();
                if (count > 0) {
                    questionList = questionMapper.selectRandomQuestionsByFolderId(questionInfo.getType(),examPaper.getCourseid(),folderInfo.getFolderId(),count);
                    questionAll.addAll(questionList);
                }
            }
            int score = questionInfo.getScore() / questionAll.size();
            for(int i=0;i<questionAll.size();i++){
                Question question = questionAll.get(i);
                PaperQuestion paperQuestion = new PaperQuestion();
                paperQuestion.setQuestionid(question.getId());
                paperQuestion.setScore(score);
                paperQuestion.setPaperid(examId);
                paperQuestion.setNum(index);
                paperQuestionMapper.insert(paperQuestion);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //私有方法
    /**
     * 从前端得到试卷信息和试卷题目信息
     * @param requestData
     */
    private void getRequestData(Map<String, Object> requestData){//获取对象请求数据
        Map<String, Object> examData = (Map<String, Object>) requestData.get("examInfo");
        ArrayList<Map<String, Object>> questionData = ( ArrayList<Map<String, Object>>) requestData.get("questionInfo");
        examPaper = new ExamPaper();
        examPaper.setName((String)examData.get("title"));
        examPaper.setScore((Integer) examData.get("score"));
        examPaper.setCount((Integer)examData.get("count"));
        examPaper.setLevel((String)examData.get("level"));
        examPaper.setCreator((String)examData.get("creator"));
        examPaper.setCreatetime((String)examData.get("createtime"));
        examPaper.setCourseid((Integer)examData.get("courseId"));
        examPaper.setType("exam");
        examPaper.setFolderid((Integer)examData.get("folderid"));

        questionInfoList = new ArrayList<>();
        for(int i=0;i<questionData.size();i++){
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setType((Integer) questionData.get(i).get("type"));
            questionInfo.setScore((Integer)questionData.get(i).get("score"));
            questionInfo.setCount((Integer)questionData.get(i).get("count"));//从题库抽题的数量
            questionInfo.setMethod((Integer) questionData.get(i).get("method"));
            int method = questionInfo.getMethod();
            int count = questionInfo.getCount();
            if(method!=-1){//如果有选择抽题方式
                if(method==1){//抽题方式为文件夹抽题
                    questionInfo.setFolderInfoList((List<FolderInfo>)examData.get("items"));
                    ArrayList<Map<String, Object>> items = ( ArrayList<Map<String, Object>>) questionData.get(i).get("items");
                    List<FolderInfo> folderInfoList = new ArrayList<>();
                    for(int j=0;j<items.size();j++){
                        FolderInfo folderInfo = new FolderInfo();
                        folderInfo.setFolderId((Integer)items.get(j).get("folderId"));
                        folderInfo.setCount((Integer) items.get(j).get("count"));//从某个文件夹抽题的数量
                        if(folderInfo.getCount()>0)//当前文件抽题数量大于0
                            folderInfoList.add(folderInfo);
                    }
                    if(folderInfoList.size()>0){//文件夹列表数大于0
                        questionInfo.setFolderInfoList(folderInfoList);
                        questionInfoList.add(questionInfo);
                    }
                }else if(count>0){//抽题方式为题库抽题，数量大于0
                    questionInfoList.add(questionInfo);
                }
            }
        }
    }

    /**
     * 获取客观题正确率
     * @param paperId
     * @param studentId
     * @return
     */
    private float calcObjectiveAccuary(int paperId,String studentId){
        int ObjectCount = paperQuestionMapper.CountObjectQuestion(paperId);
        int score = examRecordMapper.CountObjectQuestionByStudentId(studentId,paperId);
        float objectiveAccuracy = (float) score/ObjectCount*100;
        // 将浮点数四舍五入保留两位小数
        objectiveAccuracy = Math.round(objectiveAccuracy * 100.0f) / 100.0f;
        return objectiveAccuracy;
    }
}
