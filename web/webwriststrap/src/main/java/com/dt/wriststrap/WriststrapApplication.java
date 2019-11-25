package com.dt.wriststrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dt.wriststrap.mapper")
public class WriststrapApplication {
    public static void main(String[] args) {
        SpringApplication.run(WriststrapApplication.class, args);
    }
}
