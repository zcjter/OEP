package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.Content;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ContentMapper extends BaseMapper<Content> {
    Content getOneContent(int chapterId,int courseId);
    void updateContent(int chapterId,String content);

    void deleteContent(int chapterId);
}
