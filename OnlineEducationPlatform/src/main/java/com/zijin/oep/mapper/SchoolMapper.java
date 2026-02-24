package com.zijin.oep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.oep.model.School;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchoolMapper extends BaseMapper<School> {
    List<School> selectAll();
}
