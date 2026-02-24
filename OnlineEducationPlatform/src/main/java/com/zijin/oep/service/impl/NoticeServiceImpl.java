package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.NoticeMapper;
import com.zijin.oep.mapper.SystemLogMapper;
import com.zijin.oep.mapper.UserMapper;
import com.zijin.oep.model.Notice;
import com.zijin.oep.model.SystemLog;
import com.zijin.oep.model.User;
import com.zijin.oep.service.NoticeService;
import com.zijin.oep.utils.UtilTools;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {
    private UserMapper userMapper;//user表mapper
    private NoticeMapper noticeMapper;//notice表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper

    public NoticeServiceImpl(UserMapper userMapper,NoticeMapper noticeMapper,
                             SystemLogMapper systemLogMapper){
        this.userMapper = userMapper;
        this.noticeMapper = noticeMapper;
        this.systemLogMapper = systemLogMapper;
    }

    /**
     * 查询在某个班级的学生信息
     * @param classId
     */
    @Override
    public List<User> selectStudentInClass(int classId){
        return userMapper.selectStudentInClass(classId);
    }

    /**
     * 教师发布通知
     * @param requestData
     * @return
     */
    @Override
    public boolean releaseNotice(@NotNull Map<String, Object> requestData){
        Notice notice = new Notice();
        notice.setSenderid((String) requestData.get("senderid"));
        notice.setSender((String) requestData.get("sender"));
        notice.setTitle((String) requestData.get("title"));
        notice.setContent((String) requestData.get("content"));
        notice.setReleasetime(UtilTools.timeString2TimeStamp((String)requestData.get("releaseTime")));
        notice.setCourseid((int) requestData.get("courseId"));
        notice.setType((int)requestData.get("noticeType"));
        notice.setState(0);
        if(notice.getType()==0){
            ArrayList<Map<String, Object>> classData = ( ArrayList<Map<String, Object>>) requestData.get("classIdList");
            for(int i=0;i<classData.size();i++){
                int classId = (int) classData.get(i).get("id");
                String className = (String) classData.get(i).get("name");
                notice.setClassid(classId);
                notice.setClassname(className);
                noticeMapper.insert(notice);

                String userId = (String) requestData.get("senderid");
                SystemLog systemLog = new SystemLog();
                systemLog.setUserid(userId);
                systemLog.setDate(UtilTools.getCurrentTime());
                systemLog.setCourseid(notice.getCourseid());
                systemLog.setDescr("发送通知到"+className);
                //往tb_systemlog插入日志
                systemLogMapper.insert(systemLog);
            }

        }else{
            ArrayList<Map<String, Object>> studentData = ( ArrayList<Map<String, Object>>) requestData.get("studentIdList");
            for(int j=0;j<studentData.size();j++){
                String receiver = (String) studentData.get(j).get("name");
                String receiverId = (String) studentData.get(j).get("id");
                notice.setReceiver(receiver);
                notice.setReceiverid(receiverId);
                noticeMapper.insert(notice);

                String userId = (String) requestData.get("senderid");
                SystemLog systemLog = new SystemLog();
                systemLog.setUserid(userId);
                systemLog.setDate(UtilTools.getCurrentTime());
                systemLog.setCourseid(notice.getCourseid());
                systemLog.setDescr("发送通知给"+receiver);
                //往tb_systemlog插入日志
                systemLogMapper.insert(systemLog);
            }
        }

        return true;
    }

    /**
     * 查询老师发送的通知
     * @param courseId
     * @param userId
     * @return
     */
    @Override
    public List<Notice> selectByCourseIdAndUserId(int courseId,String userId){
        return noticeMapper.selectByCourseIdAndUserId(courseId,userId);
    }

    /**
     * 删除通知信息
     * @param noticeId
     */
    @Override
    public  void deleteNotice(int noticeId,String userId,int courseId){
        noticeMapper.deleteNotice(noticeId);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("删除通知");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 查询所有通知信息
     * @param userId
     * @return
     */
    @Override
    public  List<Notice> fetchNotices(String userId){
        //step1 获取收件人为学生个人的通知
        List<Notice> noticeList = noticeMapper.selectAllReceiverId(userId);
        //step2 获取收件人为班级的通知
        //step2-1 根据用户id获取所属的班级列表
        //step2-2 依次获取班级列表中所有班级的通知,追加到noticeList
        noticeList.addAll(noticeMapper.selectAllByClassId(userId));
        return noticeList;
    }
}
