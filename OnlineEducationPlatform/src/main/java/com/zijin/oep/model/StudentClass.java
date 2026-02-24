package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_studentclass")
public class StudentClass {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String sid;
    private int classid;
}
