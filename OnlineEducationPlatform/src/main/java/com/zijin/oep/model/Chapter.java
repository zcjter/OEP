package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_chapter")
public class Chapter {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String label;
    private int courseid;
    private int pid;
    private int type;
}
