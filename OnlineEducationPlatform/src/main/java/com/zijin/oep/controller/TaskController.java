package com.zijin.oep.controller;

import com.zijin.oep.model.*;
import com.zijin.oep.result.R;
import com.zijin.oep.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 作业控制器
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 保存作业信息,在新建作业保存时请求
     * @param requestData
     * @return
     */
    @PostMapping("/saveTask")
    public R saveTask(@RequestBody Map<String, Object> requestData){
        boolean ret = taskService.saveTask(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();

    }

    /**
     * 查询某份作业题目信息请求
     * @param taskId          作业id
     * @return
     */
    @PostMapping("/selectQuestionByTaskId")
    public R selectQuestionByTaskId(@RequestParam("taskId") int taskId){
        List<QuestionEx3> questionEx3List = taskService.selectQuestionByTaskId(taskId);
        if(questionEx3List!=null)
            return R.ok().setData(questionEx3List);
        else
            return R.error();
    }

    /**
     * 发布作业请求
     * @param requestData
     * @return
     */
    @PostMapping("/release")
    public R releaseTask(@RequestBody Map<String, Object> requestData){
       boolean ret = taskService.releaseTask(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();
    }

    /**
     * 获取某个班级的作业信息
     * @param classId                   班级id
     * @return
     */
    @PostMapping("/selectClassTask")
    public R selectClassTask(@RequestParam("classId") int classId){
        List<TaskReleaseEx> taskReleaseExList = taskService.selectClassTask(classId);
        if(taskReleaseExList!=null)
            return R.ok().setData(taskReleaseExList);
        else
            return R.error();
    }

    /**
     * 学生进入课程后获取课程记录信息请求
     * @param courseId               课程id
     * @param studentId              用户id
     * @return
     */
    @PostMapping("/selectTask")
    public R selectTask(@RequestParam("courseId") int courseId,
                        @RequestParam("studentId") String studentId){
        List<TaskReleaseEx1> taskReleaseEx1List = taskService.selectStudentTask(courseId,studentId);
        if(taskReleaseEx1List!=null){
            return R.ok().setData(taskReleaseEx1List);
        }else{
            return R.error();
        }
    }

    /**
     * 提交作业请求
     * @param requestData
     * @return
     */
    @PostMapping("/submitTask")
    public R submitTask(@RequestBody Map<String, Object> requestData){
        boolean ret = taskService.submitTask(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();
    }

    /**
     * 统计学生作业答题记录请求
     * @param taskId                作业id
     * @param classId               班级id
     * @return
     */
    @PostMapping("/selectTaskRecord")
    public R selectTaskRecord(@RequestParam("taskId") int taskId,
                              @RequestParam("classId") int classId){
        List<TaskRecord> taskRecordList = taskService.selectByTaskIdAndClassId(taskId,classId);
        if(taskRecordList!=null){
            return R.ok().setData(taskRecordList);
        }else{
            return R.error();
        }
    }

    /**
     * 统计已发送人数，待批阅人数，已交人数请求
     * @param taskId                 作业id
     * @param classId                班级id
     * @return
     */

    @PostMapping("/countTaskStatus")
    public R countTaskStatus(@RequestParam("taskId") int taskId,
                             @RequestParam("classId") int classId){
        Map<String,Integer> map = taskService.countTaskStatus(taskId,classId);
        if(map!=null){
            return R.ok().setData(map);
        }else{
            return R.error();
        }
    }

    /**
     * 统计某位学生记录请求
     * @param taskId                作业id
     * @param studentId             用户id
     * @return
     */
    @PostMapping("/selectTaskByTaskIdAndStudentId")
    public R selectTaskByTaskIdAndStudentId(@RequestParam("taskId") int taskId,
                                            @RequestParam("studentId") String studentId){
        List<QuestionEx4> questionEx4List = taskService.selectTaskByTaskIdAndStudentId(taskId,studentId);
        if(questionEx4List!=null){
            return R.ok().setData(questionEx4List);
        }else{
            return R.error();
        }
    }

    /**
     * 老师批改学生作业请求
     * @param requestData
     * @return
     */
    @PostMapping("/updateReviewRecord")
    public R teacherRating(@RequestBody Map<String, Object> requestData){
        boolean ret = taskService.teacherRating(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();
    }

    /**
     * 查询未交作业的学生信息
     * @param classId         班级id
     * @param taskId          作业id
     * @return
     */
    @PostMapping("/selectStudentNotInTask")
    public R selectStudentNotInTask(@RequestParam("classId") int classId,
                                    @RequestParam("taskId") int taskId){
        List<TaskRecord> taskRecordList = taskService.selectStudentNotInTask(classId,taskId);
        if(taskRecordList!=null){
            return R.ok().setData(taskRecordList);
        }else{
            return R.error();
        }
    }
}
