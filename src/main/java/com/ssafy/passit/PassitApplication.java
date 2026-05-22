package com.ssafy.passit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ssafy.passit")
public class PassitApplication {

    public static void main(String[] args) {
        SpringApplication.run(PassitApplication.class, args);
    }

}
