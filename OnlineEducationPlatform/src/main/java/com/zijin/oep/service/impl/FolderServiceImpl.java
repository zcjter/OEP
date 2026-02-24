package com.zijin.oep.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.oep.mapper.FolderMapper;
import com.zijin.oep.mapper.SystemLogMapper;
import com.zijin.oep.model.Folder;
import com.zijin.oep.model.FolderEx;
import com.zijin.oep.model.SystemLog;
import com.zijin.oep.service.FolderService;
import com.zijin.oep.utils.TreeNode1;
import com.zijin.oep.utils.UtilTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;


@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
    @Autowired
    private FolderMapper folderMapper;
    @Autowired
    private SystemLogMapper systemLogMapper;

    /**
     * 添加文件夹
     * @param folder
     * @param userId
     */
    @Override
    public void addFolder(Folder folder,String userId){
        folderMapper.insert(folder);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(folder.getCourseid());
        systemLog.setDescr("创建文件夹"+folder.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 获取题目文件夹数据
     * @param courseId
     * @param parentId
     * @return
     */
    @Override
    public List<Folder> selectFolderByCourseId(int courseId,int parentId){
        return folderMapper.selectAllByParentIdAndCourseId(courseId,parentId);
    }

    /**
     * 获取试卷文件夹数据
     * @param courseId
     * @param parentId
     * @return
     */
    @Override
    public List<Folder> selectAllByCourseIdExam(int courseId,int parentId){
        return folderMapper.selectAllByCourseIdExam(courseId,parentId);
    }

    /**
     * 获取作业文件夹数据
     * @param courseId
     * @param parentId
     * @return
     */
    @Override
    public List<Folder> selectAllByCourseIdTask(int courseId,int parentId){
        return folderMapper.selectAllByCourseIdTask(courseId,parentId);
    }

    /**
     * 获取文件夹下文件夹数据
     * @param parentId
     * @return
     */
    @Override
    public List<Folder> selectFolderByParentId(int parentId){
        return folderMapper.selectAllByParentId(parentId);
    }

    /**
     * 按照题干内容模糊查询
     * @param searchText
     * @param courseId
     * @return
     */
    @Override
    public List<Folder> getFolderName(String searchText, int courseId){
        return folderMapper.getFolderName(searchText,courseId);
    }

    /**
     * 查询所创建的题目请求
     * @param type
     * @param courseId
     * @return
     */
    @Override
    public List<FolderEx> selectAllByQuestionType(int type, int courseId){
        return folderMapper.selectAllByQuestionType(type,courseId);
    }

    /**
     * 查询所有文件夹信息
     * @param id
     * @return
     */
    @Override
    public  Folder selectFolderById(int id){
        return folderMapper.selectById(id);
    }

    /**
     * 为指定课程下的所有活动文件夹建树
     * @param courseId
     * @return
     */
    @Override
    public List<TreeNode1> selectFolderByIdBuiltTree(int courseId){
        List<Folder> folderList = folderMapper.selectAllByCourseId(courseId);
        List<TreeNode1> treeNode1List = buildTree(folderList,0);
        return treeNode1List;
    }

    /**
     * 重命名文件夹
     * @param folder
     * @param userId
     */
    @Override
    public void updateNameById(Folder folder,String userId){
        folderMapper.updateNameById(folder);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(folder.getCourseid());
        systemLog.setDescr("把文件夹改为"+folder.getName());
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 沙漠成绩文件夹以及文件夹下题目
     * @param folderId
     * @param dirty
     */
    @Override
    public void updateDirtyByIdOrParentId(int folderId, int dirty){
        folderMapper.updateDirtyByIdOrParentId(folderId,dirty);
    }

    /**
     * 移动文件夹
     * @param folderId
     * @param id
     * @param userId
     * @param courseId
     */
    @Override
    public void moveToFolder(int folderId, int id,String userId,int courseId){
        folderMapper.moveToFolder(folderId,id);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("移动一个文件夹");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //私有方法

    /**
     * 递归方法，用来创建活动文件夹树形结构数据
     * @param nodes
     * @param parentId
     * @return
     */
    private List<TreeNode1> buildTree(List<Folder> nodes, int parentId){
        List<TreeNode1> treeNode1List = new ArrayList<>();
        for (Folder node : nodes) {
            if (node.getPid() == parentId) {
                TreeNode1 treeNode1 = new TreeNode1();
                treeNode1.setId(node.getId());
                treeNode1.setName(node.getName());
                treeNode1.setFolderType(node.getFoldertype());
                treeNode1.setPid(node.getPid());
                treeNode1.setCourseid(node.getCourseid());
                treeNode1.setCreator(node.getCreator());
                treeNode1.setCreatetime(node.getCreatetime());

                // 递归构建子节点
                treeNode1.setChildren(buildTree(nodes, node.getId()));
                treeNode1List.add(treeNode1);
            }
        }
        return treeNode1List;
    }

}
