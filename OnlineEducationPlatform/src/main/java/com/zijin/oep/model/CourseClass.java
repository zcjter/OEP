package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_class")
public class CourseClass {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String name;
    private int cid;
}
