package com.zijin.oep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zijin.oep.mapper")
@SpringBootApplication
public class OEPApplication {

    public static void main(String[] args) {
        SpringApplication.run(OEPApplication.class, args);
    }

}
