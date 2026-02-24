package com.zijin.oep.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExamRecordEx {
    private int id;
    private String sid;
    private String sname;
    private int paperid;
    private Timestamp starttime;
    private Timestamp endtime;
    private int score;
    private float accuracy;
    private String ipaddress;
    private String reviewer;
    private Timestamp reviewtime;
    private String status;
    private int count;
    private int classid;
    private int total;
}
