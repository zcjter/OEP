package com.zijin.oep.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



@Data
@TableName("tb_user")
public class User {
    private String id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private int state;
    private int type;
    private int sid;
    private int pid;
}
