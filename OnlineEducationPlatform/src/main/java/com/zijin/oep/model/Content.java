package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;


@Data
@TableName("tb_content")
public class Content {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private int cid;
    private int courseid;
    private String content;
}
