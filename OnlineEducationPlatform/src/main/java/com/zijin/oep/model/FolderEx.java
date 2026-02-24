package com.zijin.oep.model;

import lombok.Data;

@Data
public class FolderEx {
    private int id;
    private String name;
    private int pid;
    private String type;
    private String createtime;
    private int courseid;
    private String creator;
    private int foldertype;
    private int dirty;
    private int count;//该文件夹下某种题型的数目
    private int selcount;//该文件夹下抽题数目
}
