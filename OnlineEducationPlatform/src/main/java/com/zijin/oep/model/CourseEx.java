package com.zijin.oep.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class CourseEx {
    private int id;
    private String name;
    private String uid;
    private int sid;
    private int pid;
    private String descr;
    private String cover;
    private String TeacherName;
    private int classId;
}
