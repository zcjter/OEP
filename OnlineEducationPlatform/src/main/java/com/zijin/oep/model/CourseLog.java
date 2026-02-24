package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tb_courselog")
public class CourseLog {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String userid;
    private int classid;
    private int courseid;
    private Timestamp date;
    private String ipaddr;
    private String descr;
}
