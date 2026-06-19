package com.ssafy.passit.user.mapper;

import com.ssafy.passit.auth.dto.RefreshTokenResult;
import com.ssafy.passit.user.dto.User;
import com.ssafy.passit.user.dto.SignupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void insert(SignupRequest signupRequest);

    int countByUsername(String username);

    int countByNickname(String nickname);

    Optional<User> findByUsername(String username);

    void updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    void clearRefreshToken(Long userId);

    RefreshTokenResult findByUserId(Long UserId);

    User findUserById(Long userId);

    void updateRoleToAdmin(String username);
}
