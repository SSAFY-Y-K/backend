package com.ssafy.passit.user.repository;

import com.ssafy.passit.user.dto.User;
import com.ssafy.passit.user.dto.SignupRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {

    void insert(SignupRequest signupRequest);

    int countByUsername(String username);

    int countByNickname(String nickname);

    Optional<User> findByUsername(String username);
}
