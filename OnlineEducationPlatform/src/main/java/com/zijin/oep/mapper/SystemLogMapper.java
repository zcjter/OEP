package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.SystemLog;
import com.zijin.oep.model.SystemLogEx;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog> {
    List<SystemLogEx> selectAllSystemLog();
    List<SystemLogEx> selectSystemLogByUserIdAndCourId(String teacherId,int courseId);
}
