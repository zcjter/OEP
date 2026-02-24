package com.zijin.oep.utils;

import lombok.Data;

import java.util.List;

//树形节点数据类,用于章节目录结构
@Data
public class TreeNode {
    private int id;
    private String label;
    private int courseid;
    private int pid;
    private int type;
    private List<TreeNode> children;
}
