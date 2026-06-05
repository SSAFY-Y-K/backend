package com.ssafy.passit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @Schema(description = "로그인 유저네임", example = "username123")
    private String username;
    @Schema(description = "로그인 비밀번호", example = "password1234")
    private String password;
}
