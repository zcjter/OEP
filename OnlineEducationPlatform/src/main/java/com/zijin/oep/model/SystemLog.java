package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tb_systemlog")
public class SystemLog {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String userid;
    private Timestamp date;
    private String ipaddr;
    private String descr;
    private int courseid;
}
