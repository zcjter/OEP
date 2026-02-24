package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tb_examrecord")
public class ExamRecord {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String sid;
    private String sname;
    private int paperid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int score;
    private float accuracy;
    private String ipaddress;
    private String reviewer;
    private Timestamp reviewtime;
    private String status;
    private int count;
    private int classid;
}
