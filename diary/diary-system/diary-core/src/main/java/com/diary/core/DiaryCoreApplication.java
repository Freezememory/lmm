package com.diary.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.diary.core.mapper")
public class DiaryCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiaryCoreApplication.class, args);
    }
}
