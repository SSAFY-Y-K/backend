package com.ssafy.passit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.ssafy.passit.user.repository",
        "com.ssafy.passit.problem.repository",
        "com.ssafy.passit.problem.mapper",
        "com.ssafy.passit.post.mapper",
        "com.ssafy.passit.submission.mapper",
        "com.ssafy.passit.user.repository",
})
public class PassitApplication {

    public static void main(String[] args) {
        SpringApplication.run(PassitApplication.class, args);
    }

}
