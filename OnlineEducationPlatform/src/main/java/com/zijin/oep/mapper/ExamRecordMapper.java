package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.ExamRecord;
import com.zijin.oep.model.ExamRecordEx;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {
    void updateExamRecord(Timestamp endTime, int paperId,String studentId,float objectiveaccuracy);
    ExamRecord selectByPaperIdAndStuId(int paperId, String studentId);
    int CountObjectQuestionByStudentId(String studentId,int paperId);
    List<ExamRecord> selectExamPaperByExamId(int paperId,int classId);
    void updateReviewRecord(String reviewer,Timestamp reviewTime,int paperId,String studentId);
    List<ExamRecordEx> selectScoreByClassId(int classId);
}
