package com.ssafy.passit.user.dto;

import lombok.Builder;

@Builder
public record SessionUser(
        Long userId,
        String username,
        String nickname,
        Role role
) {

}
