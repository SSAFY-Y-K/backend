package com.ssafy.passit.user.dto;

import com.ssafy.passit.common.type.Role;

public record UserProfileResponse(
    Long userId,
    String username,
    String nickname,
    String role
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
            user.getUserId(),
            user.getUsername(),
            user.getNickname(),
            user.getRole() == null ? null : user.getRole().name()
        );
    }
}
