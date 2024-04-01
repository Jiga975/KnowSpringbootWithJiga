package com.myfarmblog.farmnews.service;

import com.myfarmblog.farmnews.payload.requests.LoginRequest;
import com.myfarmblog.farmnews.payload.requests.UserSignUpRequest;
import com.myfarmblog.farmnews.payload.response.ApiResponse;
import com.myfarmblog.farmnews.payload.response.JwtAuthResponse;
import com.myfarmblog.farmnews.payload.response.UserSignUpResponse;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface AuthService {
    ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(UserSignUpRequest userSignUpRequest);

    ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(UserSignUpRequest userSignUpRequest);

    ResponseEntity<ApiResponse<JwtAuthResponse>> login(LoginRequest loginRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    void logout();
}
