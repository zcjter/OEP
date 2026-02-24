package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.ExamPaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {
    Integer getMaxId();
    List<ExamPaper> selectAllByFolderId(int courseId,int parentId);
    void sealExamPaper(int examPaperId,String password);
    void unsealExamPaper(int examPaperId);
}
