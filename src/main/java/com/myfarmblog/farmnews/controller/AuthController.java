package com.myfarmblog.farmnews.controller;

import com.myfarmblog.farmnews.entity.model.User;
import com.myfarmblog.farmnews.payload.requests.LoginRequest;
import com.myfarmblog.farmnews.payload.requests.UserSignUpRequest;
import com.myfarmblog.farmnews.payload.response.ApiResponse;
import com.myfarmblog.farmnews.payload.response.JwtAuthResponse;
import com.myfarmblog.farmnews.payload.response.UserSignUpResponse;
import com.myfarmblog.farmnews.repository.RoleRepository;
import com.myfarmblog.farmnews.repository.UserRepository;
import com.myfarmblog.farmnews.securitty.JwtTokenProvider;
import com.myfarmblog.farmnews.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository rolerepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;


    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return authService.registerUser(userSignUpRequest);
    }
    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<UserSignUpResponse>>registerAdmin(@Valid @RequestBody UserSignUpRequest request){
        return authService.registerAdmin(request);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return authService.login(loginRequest);
    }
    @GetMapping("/logout")
    private ResponseEntity<ApiResponse<String>> logout(){
        authService.logout();
        return ResponseEntity.ok(new ApiResponse<>("Logout Successfully"));
    }

}
