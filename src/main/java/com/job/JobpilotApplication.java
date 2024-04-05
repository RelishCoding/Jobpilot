package com.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.job.mapper")
public class JobpilotApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobpilotApplication.class, args);
    }
}
