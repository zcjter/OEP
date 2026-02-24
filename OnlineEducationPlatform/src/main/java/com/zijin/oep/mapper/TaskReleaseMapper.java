package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.TaskRelease;
import com.zijin.oep.model.TaskReleaseEx;
import com.zijin.oep.model.TaskReleaseEx1;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskReleaseMapper  extends BaseMapper<TaskRelease> {
    List<TaskReleaseEx> selectByClassId(int classId);
    List<TaskReleaseEx1> selectTaskByClassId(int classId);
}
