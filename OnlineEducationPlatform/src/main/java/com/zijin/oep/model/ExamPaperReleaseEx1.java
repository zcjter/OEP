package com.zijin.oep.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExamPaperReleaseEx1 {
    private int id;
    private int paperid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int classid;
    private String name;
    private int count;
    private String status;
}
