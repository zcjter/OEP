package com.zijin.oep.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskReleaseEx {
    private int id;
    private int taskid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int classid;
    private String name;
}
