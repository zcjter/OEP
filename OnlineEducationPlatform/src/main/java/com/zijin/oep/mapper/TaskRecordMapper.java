package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.TaskRecord;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Mapper
public interface TaskRecordMapper  extends BaseMapper<TaskRecord> {
    TaskRecord selectBySidAndTaskId(String studentId,int taskId);
    void updateStatusAndStatus(String studentId, int taskId, Timestamp endTime);
    List<TaskRecord> selectByTaskIdAndClassId(int taskId,int classId);
    Map<String, Integer> countTaskStatus(int taskId, int classId);
    void updateReviewRecord(int taskId,String studentId,String reviewer,Timestamp reviewTime);
    List<TaskRecord> selectStudentNotInTask(int classId,int taskId);
}
