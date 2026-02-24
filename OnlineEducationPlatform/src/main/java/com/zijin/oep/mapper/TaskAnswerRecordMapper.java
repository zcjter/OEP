package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.TaskAnswerRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskAnswerRecordMapper extends BaseMapper<TaskAnswerRecord> {
    void markingTask(int score,int questionId,String studentId,int taskId);
}
