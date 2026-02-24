package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.CourseClass;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseClassMapper extends BaseMapper<CourseClass> {
    List<CourseClass> selectClass(int cid);

    void rename(CourseClass courseClass);

    /**
     * 根据学生id和课程id获取学生所属课程班级
     * @param studentId
     * @param courseId
     * @return
     */
    CourseClass selectCourseClass(String studentId,int courseId);
}
