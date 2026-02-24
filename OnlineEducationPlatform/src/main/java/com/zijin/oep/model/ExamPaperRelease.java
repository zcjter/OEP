package com.zijin.oep.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
@Data
@TableName("tb_exampaperrelease")
public class ExamPaperRelease {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private int paperid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int classid;
}
