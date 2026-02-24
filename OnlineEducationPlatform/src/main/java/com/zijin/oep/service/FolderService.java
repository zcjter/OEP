package com.zijin.oep.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zijin.oep.model.Folder;
import com.zijin.oep.model.FolderEx;
import com.zijin.oep.utils.TreeNode1;

import java.util.List;

public interface FolderService extends IService<Folder> {
    void addFolder(Folder folder,String userId);
    List<Folder> selectFolderByCourseId(int courseId,int parentId);
    List<Folder> selectAllByCourseIdExam(int courseId,int parentId);
    List<Folder> selectAllByCourseIdTask(int courseId,int parentId);
    List<Folder> selectFolderByParentId(int parentId);
    List<FolderEx> selectAllByQuestionType(int type, int courseId);
    Folder selectFolderById(int id);
    List<TreeNode1> selectFolderByIdBuiltTree(int courseId);

    List<Folder> getFolderName(String searchText, int courseId);
    void updateNameById(Folder folder,String userId);
    void updateDirtyByIdOrParentId(int folderId, int dirty);

    void moveToFolder(int folderId,int id,String userId,int courseId);
}
