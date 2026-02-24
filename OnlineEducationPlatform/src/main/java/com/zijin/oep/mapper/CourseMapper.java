package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.Course;
import com.zijin.oep.model.CourseEx;
import com.zijin.oep.model.CourseEx1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    List<Course> selectByUId(String uid);
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCourse(Course course);
    Integer getMaxId();

    List<Course> selectParticipateCourse(String uid);
    List<CourseEx1> selectAlCourse();

    List<CourseEx> selectTeacherCourse(String studentId);
}
