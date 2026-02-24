package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * tb_taskrecord实体类
 */
@Data
@TableName("tb_taskrecord")
public class TaskRecord {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String sid;
    private String sname;
    private int taskid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int score;
    private String ipaddress;
    private String reviewer;
    private Timestamp reviewtime;
    private String status;
    private int classid;
}
