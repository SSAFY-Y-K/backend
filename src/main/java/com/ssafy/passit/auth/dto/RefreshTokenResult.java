package com.ssafy.passit.auth.dto;

import com.ssafy.passit.common.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResult {

    private Long userId;
    private String username;
    private Role role;
    private String refreshToken;
}
