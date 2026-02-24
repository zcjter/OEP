package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tb_notice")
public class Notice {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String senderid;
    private String sender;
    private String receiverid;
    private String receiver;
    private String title;
    private String content;
    private Timestamp releasetime;
    private int classid;
    private String classname;
    private int courseid;
    private int type;
    private int state;
}
