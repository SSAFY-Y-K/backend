package com.ssafy.passit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    @Schema(description = "아이디(유저네임)", example = "id1234")
    private String username;
    @Schema(description = "비밀번호", example = "pass1234")
    private String password;
    @Schema(description = "비밀번호 확인", example = "pass1234")
    private String confirmPassword;
    @Schema(description = "닉네임", example = "nickname123")
    private String nickname;
}
