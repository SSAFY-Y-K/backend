package com.ssafy.passit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private Role role;
}
