package com.zijin.oep.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskReleaseEx1 {
    private int id;
    private int taskid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int classid;
    private String name;
    private int score;
    private String status;
}
