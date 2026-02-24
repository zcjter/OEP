package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_answerrecord")
public class AnswerRecord {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String sid;
    private int paperid;
    private int questionid;
    private int questiontype;
    private String answer;
    private int score;
}
