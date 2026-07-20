package com.houduan.drone.rank;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.houduan.drone")
@EnableDubbo
public class RankServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RankServiceApplication.class, args);
    }
}
