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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping("SignUp")
//    public ResponseEntity<?> RegisterUser(@RequestBody UserSignUpRequest userSignUpRequest){
//        if (userRepository.existByUsername(userSignUpRequest.getUsername())){
//            return new ResponseEntity<>("username already exist",HttpStatus.BAD_REQUEST);
//        }
//        if ((userRepository.existsByEmail(userSignUpRequest.getEmail()))){
//            return new ResponseEntity<>("email already exist",HttpStatus.BAD_REQUEST);
//        }
//        User user = User.builder()
//                .name(userSignUpRequest.getName())
//                .username(userSignUpRequest.getUsername())
//                .email(userSignUpRequest.getEmail())
//                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
//                .roles(Collections.singleton(rolerepository.findByName("ROLE_ADMIN").get()))
//                .build();
//        userRepository.save(user);
//        return new ResponseEntity<>("new user created",HttpStatus.CREATED);
//    }

    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return authService.registerUser(userSignUpRequest);
    }
    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<UserSignUpResponse>>registerAdmin(@Valid @RequestBody UserSignUpRequest request){
        return authService.registerAdmin(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> userLogin(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //get token from token provider
        String token = tokenProvider.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}
