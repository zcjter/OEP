package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {
    List<PaperQuestion> selectAllNumExamPaper(int paperId,int number);

    int CountObjectQuestion(int paperId);//统计客观题分数
}
