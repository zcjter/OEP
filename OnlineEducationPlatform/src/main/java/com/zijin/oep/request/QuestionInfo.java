package com.zijin.oep.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionInfo {
    private int type;
    private int score;
    private int method;
    private int count;
    private List<FolderInfo> folderInfoList;
}
