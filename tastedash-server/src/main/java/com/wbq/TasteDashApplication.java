package com.wbq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wbq.mapper")
public class TasteDashApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasteDashApplication.class, args);
    }

}
