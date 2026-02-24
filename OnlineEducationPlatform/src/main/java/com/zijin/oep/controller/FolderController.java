package com.zijin.oep.controller;

import com.zijin.oep.model.*;
import com.zijin.oep.result.R;
import com.zijin.oep.service.ExamService;
import com.zijin.oep.service.FolderService;
import com.zijin.oep.service.QuestionService;
import com.zijin.oep.service.TaskService;
import com.zijin.oep.utils.TreeNode1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹管理控制器
 */
@RestController
@RequestMapping("/folder")
public class FolderController {
    @Autowired
    private FolderService folderService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;
    @Autowired
    private TaskService taskService;

    /**
     * 添加文件夹信息请求
     * @param folderName                文件夹名
     * @param parentId                  文件夹父id
     * @param courseId                  课程id
     * @param createTime                创建时间
     * @param creator                   创建者
     * @param type                      文件夹类型
     * @param folderType                文件夹类型
     * @param userId                    用户id
     * @return
     */
    @PostMapping("/add")
    public R addFolder(@RequestParam("FolderName") String folderName,
                       @RequestParam("parentId") int parentId,
                       @RequestParam("courseId") int courseId,
                       @RequestParam("createTime") String createTime,
                       @RequestParam("creator") String creator,
                       @RequestParam("type") String type,
                       @RequestParam("folderType") int folderType,
                       @RequestParam("userId") String userId)
    {
        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setPid(parentId);
        folder.setCourseid(courseId);
        folder.setCreatetime(createTime);
        folder.setCreator(creator);
        folder.setType(type);
        folder.setFoldertype(folderType);
        folder.setDirty(0);
        folderService.addFolder(folder,userId);
        return R.ok("添加成功");
    }

    /**
     * 查询所有创建试卷信息请求
     * @param courseId              课程id
     * @param parentId              文件夹父id
     * @return
     */
    @PostMapping("/selectAllExam")
    public R selectAllExam(@RequestParam("courseId") int courseId,
                           @RequestParam("parentId") int parentId){
        return getFolderAndExam(courseId,parentId);
    }

    /**
     * 查询所有创建题目信息请求
     * @param courseId               课程id
     * @param parentId               文件夹父id
     * @return
     */
    @PostMapping("/selectAllQuestion")
    public R selectAllQuestion(@RequestParam("courseId") int courseId,
                               @RequestParam("parentId") int parentId){
        return getFolderAndQuestion(courseId,parentId);
    }

    /**
     * 查询创建的试卷请求
     * @param courseId               课程id
     * @param parentId               文件夹父id
     * @return
     */
    @PostMapping("/selectAllTask")
    public R selectAllTask(@RequestParam("courseId") int courseId,
                          @RequestParam("parentId") int parentId){
        return getFolderAndTask(courseId,parentId);
    }

    /**
     * 查询所创建的题目请求
     * @param courseId                  课程id
     * @param type                      所属类型
     * @return
     */
    @PostMapping("/selectAllByQuestionType")
    public R selectAllByQuestionType(@RequestParam("courseId") int courseId,
                                     @RequestParam("questionType") int type){
        List<FolderEx> folderExList = folderService.selectAllByQuestionType(type,courseId);
        if(folderExList!=null){
            return R.ok().setData(folderExList);
        }else{
            return R.error();
        }
    }

    /**
     * 返回文件夹的上一级请求
     * @param courseId              课程id
     * @param parentId              文件夹父id
     * @return
     */
    @PostMapping("/goback")
    public R goBack(@RequestParam("courseId") int courseId,
                    @RequestParam("parentId") int parentId){
        Folder folder = folderService.selectFolderById(parentId);
        return getFolderAndQuestion(courseId,folder.getPid());
    }

    /**
     * 为指定课程所有活动文件夹创建树形结构请求
     * @param courseId              课程id
     * @return
     */
    @PostMapping("/activeFolderTree")
    public R selectFolderByIdBuiltTree(@RequestParam("courseId") int courseId){
        List<TreeNode1> treeNode1s = folderService.selectFolderByIdBuiltTree(courseId);
        if(treeNode1s !=null){
            return R.ok().setData(treeNode1s);
        }else{
            return R.error();
        }
    }

    /**
     * 模糊查询题目信息请求
     * @param searchText                 搜索框
     * @param courseId                   课程id
     * @return
     */
   @PostMapping("/search")
   public R searchNameOrContent(@RequestParam("searchText") String searchText,
                                @RequestParam("courseId") int courseId){
       List<Folder> folderList = folderService.getFolderName(searchText, courseId);
       List<Question> questionList = questionService.getQuestionContent(searchText, courseId);
       List<Object> combinedData = new ArrayList<>();
       if(folderList==null&&questionList==null){
           return R.error();
       }
       if(folderList!=null)
           combinedData.addAll(folderList);
       if(questionList!=null)
           combinedData.addAll(questionList);
       return R.ok().setData(combinedData);
   }

    /**
     * 重命名文件夹名请求
     * @param folderId           文件夹id
     * @param folderName         文件夹名称
     * @param userId             用户id
     * @return
     */
    @PostMapping("/updateName")
    public R updateFolderById(@RequestParam("folderId") int folderId,
                              @RequestParam("folderName") String folderName,
                              @RequestParam("userId") String userId){
        Folder folder = new Folder();
        folder.setId(folderId);
        folder.setName(folderName);
        folderService.updateNameById(folder,userId);
        return R.ok();
    }

    /**
     * 删除文件夹请求
     * @param folderId              //文件夹id
     * @param dirty                 //删除标志
     * @return
     */
    @PostMapping("/updateDirty")
    private R updateFolderAndQuestionsDirty(@RequestParam("folderId") int folderId,
                                            @RequestParam("dirty") int dirty){//递归更新更新文件夹脏位
       updateDirtyRecursive(folderId,dirty);
       return R.ok();
    }

    /**
     * 移动题目或文件夹到下一个文件夹请求
     * @param folderId               文件夹id
     * @param id                     父id
     * @param userId                 用户id
     * @param courseId               课程id
     * @return
     */
    @PostMapping("move")
    private R moveToFolder(@RequestParam("folderId") int folderId,
                           @RequestParam("id") int id,
                           @RequestParam("userId") String userId,
                           @RequestParam("courId") int courseId){
        folderService.moveToFolder(folderId,id,userId,courseId);
        return R.ok();
    }

    /**
     * 获取指定课程的题库文件夹下的文件夹和题目列表
     * @param courseId            课程id
     * @param parentId             文件夹id
     * @return
     */
    private R getFolderAndQuestion(int courseId,int parentId){
        List<Question> questions = questionService.selectAllByFolderId(parentId,courseId);
        List<Folder>  folders = folderService.selectFolderByCourseId(courseId,parentId);
        List<Object> combinedData = new ArrayList<>();
        if(questions==null&&folders==null){
            return R.error();
        }
        if(folders!=null)
            combinedData.addAll(folders);
        if(questions!=null)
            combinedData.addAll(questions);
        return R.ok().setData(combinedData);
    }

    /**
     * 获取指定课程的试卷文件夹下的文件夹和题目列表
     * @param courseId            课程id
     * @param parentId             文件夹id
     * @return
     */
    private R getFolderAndExam(int courseId,int parentId){
        List<ExamPaper> examPapers = examService.selectAllByFolderId(courseId,parentId);
        List<Folder> folders = folderService.selectAllByCourseIdExam(courseId,parentId);
        List<Object> combinedData = new ArrayList<>();
        if(examPapers==null&&folders==null)
            return R.error();
        if(folders!=null)
            combinedData.addAll(folders);
        if(examPapers!=null)
            combinedData.addAll(examPapers);
        return R.ok().setData(combinedData);
    }

    /**
     * 获取指定课程的作业文件夹下的文件夹和题目列表
     * @param courseId            课程id
     * @param parentId             文件夹id
     * @return
     */
    private R getFolderAndTask(int courseId,int parentId){
        List<Task> taskList = taskService.selectByCourseId(courseId,parentId);
        List<Folder> folders = folderService.selectAllByCourseIdTask(courseId,parentId);
        List<Object> combinedData = new ArrayList<>();
        if(taskList==null&&folders==null)
            return R.error();
        if(folders!=null)
            combinedData.addAll(folders);
        if(taskList!=null)
            combinedData.addAll(taskList);
        return R.ok().setData(combinedData);
    }

    /**
     * 删除文件夹脏位更新操作
     * @param folderId          文件夹id
     * @param dirty             删除标志
     */
    private void updateDirtyRecursive(int folderId,int dirty){
        folderService.updateDirtyByIdOrParentId(folderId,dirty);
        questionService.updateDirtyByFolderId(folderId, dirty);
        List<Folder> folderList = folderService.selectFolderByParentId(folderId);
        for(int i=0;i<folderList.size();i++){
            Folder folder = folderList.get(i);
            updateDirtyRecursive(folder.getId(),dirty);
        }
    }

}
