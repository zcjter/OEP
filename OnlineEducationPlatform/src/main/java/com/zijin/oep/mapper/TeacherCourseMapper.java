package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.TeacherCourse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherCourseMapper extends BaseMapper<TeacherCourse> {
    void deleteCourseParticipantTeacher(int courseId,String teacherId);
}
