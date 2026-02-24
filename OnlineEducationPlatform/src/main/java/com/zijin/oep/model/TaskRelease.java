package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tb_taskrelease")
public class TaskRelease {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private int taskid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int classid;
}
