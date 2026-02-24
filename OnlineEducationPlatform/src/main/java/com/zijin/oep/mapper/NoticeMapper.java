package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    List<Map<String, Object>> selectMaps(Wrapper<Notice> queryWrapper);
    List<Notice> selectByCourseIdAndUserId(int courseId,String userId);
    void deleteNotice(int noticeId);
    List<Notice> selectAllReceiverId(String userId);
    List<Notice> selectAllByClassId(String userId);

}
