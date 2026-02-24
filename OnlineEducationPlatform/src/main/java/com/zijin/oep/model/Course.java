package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_course")
public class Course {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String name;
    private String uid;
    private int sid;
    private int pid;
    private String descr;
    private String cover;

}
