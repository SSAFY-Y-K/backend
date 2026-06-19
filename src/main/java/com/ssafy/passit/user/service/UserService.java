package com.ssafy.passit.user.service;

import com.ssafy.passit.post.model.Post;
import com.ssafy.passit.submission.dto.MySubmissionResponse;
import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.dto.UserProfileResponse;
import java.util.List;

public interface UserService {

    void signup(SignupRequest signupRequest);

    UserProfileResponse getProfile(Long userId);

    List<Post> getMyPosts(Long userId);

    List<MySubmissionResponse> getMySubmissions(Long userId);
}
