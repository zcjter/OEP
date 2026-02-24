package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("tb_systemlog")
public class SystemLogEx {
    private int id;
    private String userid;
    private Timestamp date;
    private String ipaddr;
    private String descr;
    private String name;
    private int courseid;
}
