package com.zijin.oep.controller;

import com.zijin.oep.model.Notice;
import com.zijin.oep.model.User;
import com.zijin.oep.result.R;
import com.zijin.oep.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知管理控制器
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 查询课程班级学生请求
     * @param classId            班级id
     * @return
     */
    @PostMapping("/selectStudentInClass")
    public R selectStudentInClass(@RequestParam("classId") int classId){
        List<User> userList = noticeService.selectStudentInClass(classId);
        if(userList!=null){
            return R.ok().setData(userList);
        }else{
            return R.error();
        }
    }

    /**
     * 发布通知请求
     * @param requestData
     * @return
     */
    @PostMapping("/releaseNotice")
    public R releaseNotice(@RequestBody Map<String, Object> requestData){
       boolean ret = noticeService.releaseNotice(requestData);
        if(ret==true)
            return R.ok();
        else
            return R.error();

    }

    /**
     * 查询发给班级的通知信息请求
     * @param courseId
     * @return
     */
    @PostMapping("/selectByCourseIdAndUserId")
    public R selectByCourseIdAndUserId(@RequestParam("courseId") int courseId,
                                       @RequestParam("userId") String userId){
        List<Notice> noticeList = noticeService.selectByCourseIdAndUserId(courseId,userId);
        if(noticeList!=null){
            return R.ok().setData(noticeList);
        }else{
            return R.error();
        }
    }

    /**
     * 删除通知请求
     * @param noticeId
     * @return
     */

    @PostMapping("/deleteNotice")
    public R deleteNotice(@RequestParam("noticeId") int noticeId,
                          @RequestParam("userId") String userId,
                          @RequestParam("courseId") int courseId){
        noticeService.deleteNotice(noticeId,userId,courseId);
        return R.ok();
    }

    /**
     * 查询某一用户的通知信息请求
     * @param userId
     * @return
     */
    @PostMapping("/fetchNotices")
    public R fetchNotices(@RequestParam("userId") String userId){
        List<Notice> noticeList = noticeService.fetchNotices(userId);
        if(noticeList!=null){
            return R.ok().setData(noticeList);
        }else{
            return R.error();
        }
    }
}
