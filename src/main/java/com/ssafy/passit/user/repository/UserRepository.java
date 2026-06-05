package com.ssafy.passit.user.repository;

import com.ssafy.passit.user.dto.SignupRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    void insert(SignupRequest signupRequest);

    int countByUsername(String username);

    int countByNickname(String nickname);
}
