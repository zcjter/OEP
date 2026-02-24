package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("tb_folder")
public class Folder {
    @TableId(value = "id", type = IdType.INPUT)
    private int id;
    private String name;
    private int pid;
    private String type;
    private String createtime;
    private int courseid;
    private String creator;
    private int foldertype;
    private int dirty;
}
