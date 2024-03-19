package com.myfarmblog.farmnews.controller;

import com.myfarmblog.farmnews.entity.Role;
import com.myfarmblog.farmnews.entity.User;
import com.myfarmblog.farmnews.payload.requests.LoginRequest;
import com.myfarmblog.farmnews.payload.requests.SignUpRequest;
import com.myfarmblog.farmnews.repository.RoleRepository;
import com.myfarmblog.farmnews.repository.UserRepository;
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

    @PostMapping("SignUp")
    public ResponseEntity<?> RegisterUser(@RequestBody SignUpRequest signUpRequest){
        if (userRepository.existByUsername(signUpRequest.getUsername())){
            return new ResponseEntity<>("username already exist",HttpStatus.BAD_REQUEST);
        }
        if ((userRepository.existsByEmail(signUpRequest.getEmail()))){
            return new ResponseEntity<>("email already exist",HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Collections.singleton(rolerepository.findByName("ROLE_ADMIN").get()))
                .build();
        userRepository.save(user);
        return new ResponseEntity<>("new user created",HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("sign successful", HttpStatus.OK);
    }
}
