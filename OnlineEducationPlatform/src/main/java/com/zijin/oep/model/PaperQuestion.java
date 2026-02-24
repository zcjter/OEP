package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_paperquestion")
public class PaperQuestion {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private int paperid;
    private int questionid;
    private int score;
    private int num;
}
