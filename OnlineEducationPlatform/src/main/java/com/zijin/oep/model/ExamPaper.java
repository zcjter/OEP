package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_exampaper")
public class ExamPaper {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String name;
    private int score;
    private int count;
    private String level;
    private int courseid;
    private String creator;
    private String createtime;
    private String type;
    private int folderid;
    private int sealflag;
    private String password;
}
