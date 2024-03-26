package com.myfarmblog.farmnews.service;

import com.myfarmblog.farmnews.payload.requests.UserSignUpRequest;
import com.myfarmblog.farmnews.payload.response.ApiResponse;
import com.myfarmblog.farmnews.payload.response.UserSignUpResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<UserSignUpResponse>>registerUser(UserSignUpRequest userSignUpRequest);
    ResponseEntity<ApiResponse<UserSignUpResponse>>registerAdmin(UserSignUpRequest userSignUpRequest);
}
