package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.CourseLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseLogMapper extends BaseMapper<CourseLog> {
    int countCourseContent(int classId);
    List<Map<String, Object>> getLogStatistics(int year, int month, int classId);
    List<Map<String, Object>> getLogStatisticsByStudent(int year, int month, String userId);
}
