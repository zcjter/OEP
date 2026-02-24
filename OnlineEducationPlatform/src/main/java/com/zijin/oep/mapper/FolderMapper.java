package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.Folder;
import com.zijin.oep.model.FolderEx;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder> {
    List<Folder> selectAllByParentIdAndCourseId(int courseId,int parentId);
    List<Folder> selectAllByCourseIdExam(int courseId,int parentId);
    List<Folder> selectAllByCourseIdTask(int courseId,int parentId);
    List<Folder> selectAllByCourseId(int courseId);
    List<Folder> selectAllByParentId(int parentId);
    List<Folder> getFolderName(String searchText, int courseId);
    List<FolderEx> selectAllByQuestionType(int type,int courseId);
    void updateNameById(Folder folder);
    void updateDirtyByIdOrParentId(int folderId, int dirty);
    void moveToFolder(int folderId, int id);
}

