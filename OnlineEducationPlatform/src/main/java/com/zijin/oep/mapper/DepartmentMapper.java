package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    List<Department> selectAll();
}
