package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.ExamPaperRelease;
import com.zijin.oep.model.ExamPaperReleaseEx;
import com.zijin.oep.model.ExamPaperReleaseEx1;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamPaperReleaseMapper extends BaseMapper<ExamPaperRelease> {
    List<ExamPaperReleaseEx> selectExamPaperByClassId(int classId);
    List<ExamPaperReleaseEx1> selectAllByClassId(int classId);
}
