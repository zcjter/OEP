package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_teachercourse")
public class TeacherCourse {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String tid;
    private int courseid;
    private int role;
}
