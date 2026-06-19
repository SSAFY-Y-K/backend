package com.ssafy.passit.admin;

import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.nickname}")
    private String adminNickname;

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (userMapper.countByUsername(adminUsername) > 0) {
                return;
            }

            SignupRequest request = new SignupRequest();
            request.setUsername(adminUsername);
            request.setPassword(passwordEncoder.encode(adminPassword));
            request.setNickname(adminNickname);

            userMapper.insert(request);
            userMapper.updateRoleToAdmin(adminUsername);

            log.info("관리자 계정이 생성되었습니다. username={}", adminUsername);
        } catch (Exception e) {
            log.warn("관리자 계정 초기화 실패 (DB 연결 확인 필요): {}", e.getMessage());
        }
    }
}
