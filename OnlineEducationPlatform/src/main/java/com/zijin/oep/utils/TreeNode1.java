package com.zijin.oep.utils;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode1 {
    private int id;
    private String name;
    private int folderType;
    private int pid;
    private int courseid;
    private String creator;
    private String createtime;

    private List<TreeNode1> children;
}
