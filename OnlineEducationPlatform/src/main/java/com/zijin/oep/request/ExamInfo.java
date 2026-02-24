package com.zijin.oep.request;

import lombok.Data;

@Data
public class ExamInfo {
    private String title;
    private int score;
    private int count;
    private String level;
    private int courseId;
}
