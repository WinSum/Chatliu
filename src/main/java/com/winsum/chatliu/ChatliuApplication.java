package com.winsum.chatliu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.winsum.chatliu.mapper") //找到mapper
public class ChatliuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatliuApplication.class, args);
    }

}
