package com.zijin.oep.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_question")
public class Question {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String type;
    private String level;
    private String content;
    private String answer;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int folderid;
    private String createtime;
    private String creator;
    private int questiontype;
    private int dirty;
    private int courseid;
}
