package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.AnswerRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerRecordMapper extends BaseMapper<AnswerRecord> {
    void automaticScoring(int questionType,String studentId);
    void reviewSubjectiveQuestions(int score,String studentId,int paperId,int questionId);
}
