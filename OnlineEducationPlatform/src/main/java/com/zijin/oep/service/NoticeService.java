package com.zijin.oep.service;

import com.zijin.oep.model.Notice;
import com.zijin.oep.model.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    List<User> selectStudentInClass(int classId);
    boolean releaseNotice(@NotNull Map<String, Object> requestData);
    List<Notice> selectByCourseIdAndUserId(int courseId,String userId);
    void deleteNotice(int noticeId,String userId,int courseId);
    List<Notice> fetchNotices(String userId);
}
