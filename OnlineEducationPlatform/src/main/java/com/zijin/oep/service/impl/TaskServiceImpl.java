package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.*;
import com.zijin.oep.model.*;
import com.zijin.oep.service.TaskService;
import com.zijin.oep.utils.UtilTools;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskMapper taskMapper;//作业表mapper
    private QuestionMapper questionMapper;//题目表mapper
    private TaskQuestionMapper taskQuestionMapper;//作业题目表mapper
    private TaskReleaseMapper taskReleaseMapper;//作业提交表mapper
    private UserMapper userMapper;//用户表mapper
    private TaskRecordMapper taskRecordMapper;//任务记录表mapper
    private CourseClassMapper courseClassMapper;//课程班级表mapper
    private TaskAnswerRecordMapper taskAnswerRecordMapper;//学生作业答题记录表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper
    private CourseLogMapper courseLogMapper;//课程日志表mapper

    public TaskServiceImpl(TaskMapper taskMapper, TaskQuestionMapper taskQuestionMapper, QuestionMapper questionMapper,
                           TaskReleaseMapper taskReleaseMapper, UserMapper userMapper, TaskRecordMapper taskRecordMapper,
                           CourseClassMapper courseClassMapper, TaskAnswerRecordMapper taskAnswerRecordMapper,
                           SystemLogMapper systemLogMapper,CourseLogMapper courseLogMapper){
        this.taskMapper = taskMapper;
        this.taskQuestionMapper = taskQuestionMapper;
        this.questionMapper = questionMapper;
        this.taskReleaseMapper = taskReleaseMapper;
        this.userMapper = userMapper;
        this.taskRecordMapper = taskRecordMapper;
        this.courseClassMapper = courseClassMapper;
        this.taskAnswerRecordMapper = taskAnswerRecordMapper;
        this.systemLogMapper = systemLogMapper;
        this.courseLogMapper = courseLogMapper;
    }

    /**
     * 保存作业信息
     * @param requestData
     * @return
     */
    @Override
    public boolean saveTask(@NotNull Map<String, Object> requestData){
        //step1 把作业数据插入tb_task
        Task task = new Task();
        task.setName((String) requestData.get("taskName"));
        task.setScore((int) requestData.get("totalScore"));
        task.setCourseid((int)requestData.get("courseId"));
        task.setCreatetime((String) requestData.get("createTime"));
        task.setCreator((String) requestData.get("creator"));
        task.setFolderid((int)requestData.get("folderId"));
        task.setType("task");
        taskMapper.insert(task);
        //step2 把作业中的每道题数据插入tb_taskquestion
        int taskId = taskMapper.getMaxId();//获取作业id
        TaskQuestion taskQuestion = new TaskQuestion();
        taskQuestion.setTaskid(taskId);//设置作业id
        ArrayList<Map<String, Object>> taskData = ( ArrayList<Map<String, Object>>) requestData.get("questions");
        for(int i=0;i<taskData.size();i++){
            taskQuestion.setQuestionid((int)taskData.get(i).get("id"));//设置题目id
            taskQuestion.setScore((int)taskData.get(i).get("score"));//设置题目分数
            taskQuestionMapper.insert(taskQuestion);
        }
        String userId = (String) requestData.get("userId");
        Task task1 = taskMapper.selectById(taskId);


        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(task.getCourseid());
        systemLog.setDescr("发布"+task1.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
        return true;
    }

    /**
     * 获取试卷表试卷信息
     * @param courseId
     * @param parentId
     * @return
     */
    @Override
    public List<Task> selectByCourseId(int courseId,int parentId){
        return taskMapper.selectByCourseId(courseId,parentId);
    }

    /**
     * 获取某份作业信息
     * @param taskId
     * @return
     */
    @Override
    public  List<QuestionEx3> selectQuestionByTaskId(int taskId){
        return questionMapper.selectQuestionByTaskId(taskId);
    }

    /**
     * 作业发布服务
     * @param requestData
     */
    @Override
    public boolean releaseTask(@NotNull Map<String, Object> requestData){
        //往tb_taskrelease表插入作业发布记录(多个班级)
        TaskRelease taskRelease = new TaskRelease();
        taskRelease.setTaskid((Integer)requestData.get("taskId"));
        taskRelease.setStarttime(UtilTools.timeString2TimeStamp((String)requestData.get("startTime")));
        taskRelease.setEndtime(UtilTools.timeString2TimeStamp((String)requestData.get("endTime")));
        ArrayList<Object> classData = ( ArrayList<Object>) requestData.get("classIds");
        for(int i=0;i<classData.size();i++){
            int classId = (int) classData.get(i);
            taskRelease.setClassid(classId);
            //step1 插入班级作业发布记录
            taskReleaseMapper.insert(taskRelease);
            //step2 根据班级id查询学生列表信息
            TaskRecord taskRecord = new TaskRecord();
            taskRecord.setTaskid(taskRelease.getTaskid());
            taskRecord.setStarttime(taskRelease.getStarttime());
            taskRecord.setStatus("未交");
            taskRecord.setClassid(classId);
            List<User> userList = userMapper.selectStudentInClass(classId);
            //step3 为每位学生在tb_taskrecord中插入记录
            for(int j=0;j<userList.size();j++){
                taskRecord.setSid(userList.get(j).getId());
                taskRecord.setSname(userList.get(j).getName());
                taskRecordMapper.insert(taskRecord);
            }
            String userId = (String) requestData.get("userId");
            int taskId = (int)requestData.get("taskId");
            int courseId = (Integer) requestData.get("courseId");
            Task task = taskMapper.selectById(taskId);

            SystemLog systemLog = new SystemLog();
            systemLog.setUserid(userId);
            systemLog.setDate(UtilTools.getCurrentTime());
            systemLog.setCourseid(courseId);
            systemLog.setDescr("发布"+task.getName());
        }
        return true;
    }

    /**
     * 查询某个班级的作业记录
     * @param classId
     * @return
     */
    @Override
    public List<TaskReleaseEx> selectClassTask(int classId){
        return taskReleaseMapper.selectByClassId(classId);
    }

    /**
     * 统计某个学生的作业
     * @param courseId
     * @param studentId
     * @return
     */
    @Override
    public List<TaskReleaseEx1> selectStudentTask(int courseId,String studentId){
        //step1 根据学生id和课程id获取学生所在课程班级信息
        CourseClass courseClass = courseClassMapper.selectCourseClass(studentId,courseId);
        //step2 根据班级id获取作业信息
        List<TaskReleaseEx1> taskReleaseEx1List= taskReleaseMapper.selectTaskByClassId(courseClass.getId());
        ///step3 根据studentId和taskId查询tb_taskrecord的状态字段
        for(int i=0;i<taskReleaseEx1List.size();i++){
            int taskId = taskReleaseEx1List.get(i).getTaskid();
            TaskRecord taskRecord = taskRecordMapper.selectBySidAndTaskId(studentId,taskId);
            taskReleaseEx1List.get(i).setStatus(taskRecord.getStatus());
        }
        return taskReleaseEx1List;
    }

    /**
     * 提交作业
     * @param requestData
     * @return
     */
    @Override
    public boolean submitTask(@NotNull Map<String, Object> requestData){
        TaskAnswerRecord taskAnswerRecord = new TaskAnswerRecord();
        taskAnswerRecord.setSid((String)requestData.get("studentId"));
        taskAnswerRecord.setTaskid((int)requestData.get("taskId"));
        taskAnswerRecord.setScore(0);
        ArrayList<Map<String, Object>> AnswerData = ( ArrayList<Map<String, Object>>) requestData.get("answerList");
        for(int i=0;i<AnswerData.size();i++){
                int questionType = (int) AnswerData.get(i).get("type");
                int questionId = (int) AnswerData.get(i).get("id");
                String answer="";
                if(questionType!=1){
                    answer = (String)AnswerData.get(i).get("answerText");
                }else{
                    ArrayList<String> L = (ArrayList<String>)AnswerData.get(i).get("selectOptions");
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
                taskAnswerRecord.setQuestionid(questionId);
                taskAnswerRecord.setQuestiontype(questionType);
                taskAnswerRecord.setAnswer(answer);
                //step1 插入某道题的答题记录
                taskAnswerRecordMapper.insert(taskAnswerRecord);
                //step2 更新tb_taskrecord表
                // 获取前端传递过来的时间戳字符串
                Timestamp currentTime = UtilTools.timeString2TimeStamp((String)requestData.get("currentTime"));
                taskRecordMapper.updateStatusAndStatus(taskAnswerRecord.getSid(),taskAnswerRecord.getTaskid(),currentTime);
        }
        String studentId = (String) requestData.get("studentId");
        int courseId = (int)requestData.get("courseId");
        int classId = (int)requestData.get("classId");
        int taskId = (int)requestData.get("taskId");
        Task task = taskMapper.selectById(taskId);
        User user = userMapper.selectById(studentId);

        CourseLog courseLog = new CourseLog();
        courseLog.setUserid(studentId);
        courseLog.setDate(UtilTools.getCurrentTime());
        courseLog.setCourseid(courseId);
        courseLog.setClassid(classId);
        courseLog.setDescr(user.getName()+"完成"+task.getName());
        //往tb_courselog插入数据
        courseLogMapper.insert(courseLog);
        return true;
    }

    /**
     * 统计学生答题记录
     * @param taskId
     * @param classId
     * @return
     */
    @Override
    public List<TaskRecord> selectByTaskIdAndClassId(int taskId, int classId){
        return taskRecordMapper.selectByTaskIdAndClassId(taskId,classId);
    }

    /**
     * 统计已发送人数，待批阅人数，已交人数
     * @param taskId
     * @param classId
     * @return
     */
    @Override
    public Map<String, Integer> countTaskStatus(int taskId, int classId) {
        return taskRecordMapper.countTaskStatus(taskId, classId);
    }

    /**
     * 统计某位学生的答题记录
     * @param taskId
     * @param studentId
     * @return
     */
    @Override
    public List<QuestionEx4> selectTaskByTaskIdAndStudentId(int taskId, String studentId){
        return questionMapper.selectByTaskIdAndStudentId(taskId,studentId);
    }

    /**
     * 老师改卷
     * @param requestData
     * @return
     */
    @Override
    public boolean teacherRating(@NotNull Map<String, Object> requestData){
        ArrayList<Map<String, Object>> TaskData = ( ArrayList<Map<String, Object>>) requestData.get("subjectQuestion");
        int taskId = (int) requestData.get("taskId");
        String studentId = (String) requestData.get("studentId");
        for(int i=0;i<TaskData.size();i++){
            int questionId = (int) TaskData.get(i).get("questionId");
            int score = (int) TaskData.get(i).get("score");
            //step1 计算学生题目分数
            taskAnswerRecordMapper.markingTask(score,questionId,studentId,taskId);
        }
        // 获取前端传递过来的时间戳字符串
        Timestamp reviewTime = UtilTools.timeString2TimeStamp((String)requestData.get("reviewTime"));
        String reviewer = (String) requestData.get("reviewer");
        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setReviewer(reviewer);
        //step2 修改考试状态，存入批卷人信息
        taskRecordMapper.updateReviewRecord(taskId,studentId,reviewer,reviewTime);
        String teacherId = (String) requestData.get("teacherId");
        int courseId = (Integer) requestData.get("courseId");
        User user = userMapper.selectById(studentId);
        Task task = taskMapper.selectById(taskId);

        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(teacherId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setId(courseId);
        systemLog.setDescr("批改"+user.getName()+task.getName());
        //往tb_systemlog数据
        systemLogMapper.insert(systemLog);

        return true;
    }

    /**
     * 未提交作业的学生
     * @param classId
     * @return
     */
    @Override
    public List<TaskRecord> selectStudentNotInTask(int classId,int taskId){
        return taskRecordMapper.selectStudentNotInTask(classId,taskId);
    }
}
