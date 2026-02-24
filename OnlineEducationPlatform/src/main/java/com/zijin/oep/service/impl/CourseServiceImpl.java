package com.zijin.oep.service.impl;

import com.zijin.oep.mapper.*;
import com.zijin.oep.model.*;
import com.zijin.oep.service.CourseService;
import com.zijin.oep.utils.TreeNode;
import com.zijin.oep.utils.UtilTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class CourseServiceImpl  implements CourseService {

    private CourseMapper courseMapper;//课程表mapper
    private TeacherCourseMapper teacherCourseMapper;//教师课程关联表mapper
    private ChapterMapper chapterMapper;//章节表mapper
    private ContentMapper contentMapper;//内容表mapper
    private UserMapper userMapper;//用户表mapper
    private SystemLogMapper systemLogMapper;//系统日志表mapper
    private CourseLogMapper courseLogMapper;//课程日志表mapper
    private ExamRecordMapper examRecordMapper;//考试记录表mapper

    @Value("${custom.upload.dir}")
    private String uploadDir;


    public CourseServiceImpl(CourseMapper courseMapper,TeacherCourseMapper teacherCourseMapper,
                             ChapterMapper chapterMapper,ContentMapper contentMapper,
                             UserMapper userMapper,SystemLogMapper systemLogMapper,CourseLogMapper courseLogMapper,
                             ExamRecordMapper examRecordMapper){
        this.courseMapper = courseMapper;
        this.teacherCourseMapper = teacherCourseMapper;
        this.chapterMapper = chapterMapper;
        this.contentMapper = contentMapper;
        this.userMapper = userMapper;
        this.systemLogMapper = systemLogMapper;
        this.courseLogMapper = courseLogMapper;
        this.examRecordMapper = examRecordMapper;
    }


/////////////////////////////////////////////////////////////////////////////////
// 服务方法

    /**
     * 创建课程服务
     * @param coverFile
     * @param course
     * @param teaId
     * @return
     */
    @Override
    public boolean createCourse(MultipartFile coverFile,Course course,String teaId){
        //step1 存储封面文件
        String fileName = storeCoverFile(coverFile);
        course.setCover(fileName);
        //step2 创建课程
        courseMapper.insertCourse(course);//插入课程
        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setCourseid(courseMapper.getMaxId());
        teacherCourse.setTid(teaId);
        teacherCourse.setRole(0);//创建者
        //教师课程管理表插入记录
        teacherCourseMapper.insert(teacherCourse);
        //往系统数据库插入日志
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(teaId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(course.getId());
        systemLog.setDescr("创建课程"+course.getName());
        systemLogMapper.insert(systemLog);

        return true;
    }

    /**
     * 获取教师所教的课程列表服务
     * @param uid
     * @return
     */
    @Override
    public List<Course> fetchCourseList(String uid){
        List<Course> courseList = courseMapper.selectByUId(uid);
        for(int i=0;i<courseList.size();i++){
            Course c = courseList.get(i);
            String coverUrl= "http://localhost:8080/uploads/"+c.getCover();//组装后台服务器图片url
            c.setCover(coverUrl);
        }
        return courseList;
    }

    /**
     * 获取教师参与的课程列表服务
     * @param uid
     * @return
     */
    @Override
    public List<Course> fetchParticipateCourseList(String uid){
        List<Course> courseList= courseMapper.selectParticipateCourse(uid);
        for(int i=0;i<courseList.size();i++){
            Course c = courseList.get(i);
            String coverUrl= "http://localhost:8080/uploads/"+c.getCover();//组装后台服务器图片url
            c.setCover(coverUrl);
        }
        return courseList;
    }

    /**
     * 获取学生的课程列表服务
     * @param studentId
     * @return
     */
    @Override
    public List<CourseEx> fetchStudentCourseList(String studentId){
        List<CourseEx> courseList = courseMapper.selectTeacherCourse(studentId);
        for(int i=0;i<courseList.size();i++){
            CourseEx c = courseList.get(i);
            String coverUrl= "http://localhost:8080/uploads/"+c.getCover();//组装后台服务器图片url
            c.setCover(coverUrl);
        }
        return courseList;
    }

    /**
     * 获取课程章节列表服务
     * @param courseId
     * @return
     */
    @Override
    public List<TreeNode> fetchChapter(int courseId){
        // 从数据库或其他数据源获取原始数据
        List<Chapter> chapters = chapterMapper.getChapterListByCourseId(courseId);
        // 将原始数据转换为树形结构
        List<TreeNode> treeData = buildTree(chapters, 0);
        return treeData;
    }

    /**
     * 获取指定章节内容服务
     * @param chapterId
     * @param courseId
     * @return
     */
    @Override
    public Content fetchChapterContent(int chapterId, int courseId,int classId,String userId,int role){
        if(role==0){//只有学生角色写日志
            CourseLog courseLog = new CourseLog();
            courseLog.setUserid(userId);
            courseLog.setCourseid(courseId);
            courseLog.setDate(UtilTools.getCurrentTime());
            courseLog.setClassid(classId);
            User user = userMapper.selectById(userId);
            courseLog.setDescr(user.getName()+"查看课程");
            //往tb_courselog插入数据
            courseLogMapper.insert(courseLog);

        }

        return contentMapper.getOneContent(chapterId,courseId);

    }

    /**
     * 老师编辑查看课程信息
     * @param chapterId
     * @param courseId
     * @return
     */
    @Override
    public Content fetchChapterTeacherContent(int chapterId,int courseId){
        return contentMapper.getOneContent(chapterId,courseId);
    }


    /**
     * 新增章节内容服务
     * @param chapterId
     * @param contents
     * @param userId
     */
    @Override
    public void insertContent( int chapterId,Content contents,String userId){
        chapterMapper.updateType(chapterId);
        contentMapper.insert(contents);

        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(contents.getCourseid());
        systemLog.setDescr("新增一个章节内容");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 更新章节内容内容
     * @param chapterId
     * @param content
     * @param userId
     * @param courseId
     */
    @Override
    public void updateContent(int chapterId,String content,String userId,int courseId){
        contentMapper.updateContent(chapterId,content);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("更新一个章节内容");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 调整章节内容服务
     * @param parentId
     * @param id
     * @param userId
     * @param courseId
     */
    @Override
    public void adjustChapter(int parentId, int id,String userId,int courseId){
        chapterMapper.adjustChapter(parentId,id);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("调整章节位置");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 插入新章节服务
     * @param chapter
     * @param userId
     */
    @Override
    public void insertChapter(Chapter chapter,String userId){
        chapterMapper.insert(chapter);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(chapter.getCourseid());
        systemLog.setDescr("新增一个章节");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 重命名指定章节名称服务
     * @param name
     * @param id
     * @param userId
     * @param courseId
     */
    @Override
    public  void renameChapterName(String name, int id,String userId,int courseId){
        chapterMapper.renameChapterName(name,id);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("重命名一个章节");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);

    }

    /**
     * 删除指定章节及内容服务
     * @param chapterId
     * @param type
     * @param userId
     * @param courseId
     */
    @Override
    public void deleteContentAndChapter(int chapterId,int type,String userId,int courseId){
        if(type==1)
            contentMapper.deleteContent(chapterId);
        chapterMapper.deleteChapter(chapterId);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        systemLog.setDescr("删除章节和内容");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }


    /**
     * 查询课程教师信息服务
     * @param courseId
     * @return
     */
    @Override
    public List<User> selectTeacher(int courseId){
        return userMapper.selectTeacher(courseId);
    }

    /**
     *  批量添加教师服务
     * @param dataList
     * @param courseId
     * @param userId
     */
    @Override
    public void addTeachers( List<String> dataList,int courseId,String userId){
        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setCourseid(courseId);
        teacherCourse.setRole(1);
        for(String TeacherId:dataList){
            teacherCourse.setTid(TeacherId);
            teacherCourseMapper.insert(teacherCourse);
        }
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(userId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(teacherCourse.getCourseid());
        Course course = courseMapper.selectById(teacherCourse.getCourseid());
        systemLog.setDescr("往"+course.getName()+"插入参与教师");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);
    }

    /**
     * 模糊查询教师信息服务
     * @param searchText
     * @param courseId
     * @return
     */
    @Override
    public List<User> getTeacherNotInCourseByNameOrNO(String searchText,int courseId){
        return userMapper.getTeacherNotInCourseByNameOrNO(searchText,courseId);
    }

    /**
     * 查询不在课程里的部门教师信息服务
     * @param pid
     * @param courseId
     * @return
     */
    @Override
    public List<User> selectTeacherByDepartment(int pid,int courseId){
        return userMapper.selectTeacherByDepartment(pid,courseId);
    }

    /**
     * 查询课程教师信息服务
     * @param courseId
     * @return
     */
    @Override
    public List<User> selectTeacherByCourse(int courseId){
        return userMapper.selectTeacherByCourse(courseId);
    }

    /**
     * 删除教师信息服务
     * @param courseId
     * @param teacherId
     */
    @Override
    public void deleteCourseParticipantTeacher(int courseId,String teacherId){
        teacherCourseMapper.deleteCourseParticipantTeacher(courseId,teacherId);
        SystemLog systemLog = new SystemLog();
        systemLog.setUserid(teacherId);
        systemLog.setDate(UtilTools.getCurrentTime());
        systemLog.setCourseid(courseId);
        Course course = courseMapper.selectById(courseId);
        systemLog.setDescr("删除"+course.getName()+"中的参与教师");
        //往tb_systemlog插入日志
        systemLogMapper.insert(systemLog);

    }

    /**
     * 查询老师课程日志
     * @param teacherId
     * @param courseId
     * @return
     */
    public List<SystemLogEx> selectSystemLogByUserIdAndCourseId(String teacherId,int courseId){
        return systemLogMapper.selectSystemLogByUserIdAndCourId(teacherId,courseId);
    }

    /**
     * 统计一个班级学生访问该课程的章节内容数
     * @param classId
     * @return
     */
    public  int countCourseContent(int classId){
        return courseLogMapper.countCourseContent(classId);
    }

    /**
     * 统计某个班级每个学生的总分
     * @param classId
     * @return
     */
    public List<ExamRecordEx> selectScoreByClassId(int classId){
        return examRecordMapper.selectScoreByClassId(classId);
    }

    /**
     * 获取每一年，每一个下每个班的记录
     * @param year
     * @param month
     * @param classId
     * @return
     */
    public List<Map<String, Object>> getLogStatistics(int year, int month, int classId){
        return courseLogMapper.getLogStatistics(year,month,classId);
    }
/////////////////////////////////////////////////////////////////////////////////
// 私有方法
    /**
     * 上传课程封面图片文件方法
     * @param file
     * @return
     */
    private String storeCoverFile(MultipartFile file) {
        try {
            // 构建上传文件的目标路径
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            // 确保目标路径存在，如果不存在则创建
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            //生成新的文件名
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileName = fileName.substring(fileName.lastIndexOf('.'));
            String newFileName = UUID.randomUUID().toString() + fileName;

            // 构建文件路径
            Path filePath = uploadPath.resolve(newFileName);

            // 将MultipartFile对象的数据拷贝到指定的上传目录中
            Files.copy(file.getInputStream(), filePath);

            return newFileName;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), ex);
        }
    }
    /**
     * 创建章节树方法
     * @param nodes
     * @param parentId
     * @return
     */
    private List<TreeNode> buildTree(List<Chapter> nodes, int parentId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Chapter node : nodes) {
            if (node.getPid() == parentId) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(node.getId());
                treeNode.setPid(node.getPid());
                treeNode.setLabel(node.getLabel());
                treeNode.setCourseid(node.getCourseid());
                treeNode.setType(node.getType());
                // 递归构建子节点
                treeNode.setChildren(buildTree(nodes, node.getId()));
                treeNodes.add(treeNode);
            }
        }
        return treeNodes;
    }
}
