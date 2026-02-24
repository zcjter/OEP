package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.Chapter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
    List<Chapter> getChapterListByCourseId(int courseId);

    void renameChapterName(String name, int id);

    void deleteChapter(int id);
    void adjustChapter(int parentId, int id);
    void updateType(int id);
}
