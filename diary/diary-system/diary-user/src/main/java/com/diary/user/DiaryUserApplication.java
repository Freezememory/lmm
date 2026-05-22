package com.diary.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.diary.user.mapper")
public class DiaryUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryUserApplication.class, args);
    }
}
