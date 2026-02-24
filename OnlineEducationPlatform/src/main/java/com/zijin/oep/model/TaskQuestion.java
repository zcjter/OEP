package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_taskquestion")
public class TaskQuestion {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private int taskid;
    private int questionid;
    private int score;
}
