package com.wang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.wang.*.mapper"})
public class ChangkuguanliApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangkuguanliApplication.class, args);
    }

}
