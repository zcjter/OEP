package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.StudentClass;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentClassMapper extends BaseMapper<StudentClass> {
    void removeStudent(int classid, String sid);
    int countStudentsByClassId(int classId);
}
