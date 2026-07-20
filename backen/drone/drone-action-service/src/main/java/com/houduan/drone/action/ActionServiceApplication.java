package com.houduan.drone.action;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.houduan.drone")
@EnableDubbo
@MapperScan("com.houduan.drone.action.Mapper")
public class ActionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActionServiceApplication.class, args);
    }
}
