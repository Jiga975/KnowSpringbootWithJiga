package com.myfarmblog.farmnews.service.impl;

import com.myfarmblog.farmnews.entity.enums.Role;
import com.myfarmblog.farmnews.entity.model.User;
import com.myfarmblog.farmnews.exceptions.APIException;
import com.myfarmblog.farmnews.payload.requests.UserSignUpRequest;
import com.myfarmblog.farmnews.payload.response.ApiResponse;
import com.myfarmblog.farmnews.payload.response.UserSignUpResponse;
import com.myfarmblog.farmnews.repository.UserRepository;
import com.myfarmblog.farmnews.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImp implements AuthService {
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(UserSignUpRequest userSignUpRequest) {
        //check if user already exist by using the email
        boolean isExist = userRepository.existsByEmail(userSignUpRequest.getEmail());
        //throw error if the email already exist
        if (isExist){
            throw new APIException("user with "+ userSignUpRequest.getEmail()+ " already exist in our system");
        }
        //map new user from userRequest to user entity
        User newUser = mapper.map(userSignUpRequest,User.class);
        // give new user role
        newUser.setRole(Role.USER);
        //Encrypt password using Bcrypt passwordEncoder
        newUser.setPassword(encoder.encode(userSignUpRequest.getPassword()));
        //save user to database
        User savedUser = userRepository.save(newUser);
        //convert saved user to user response for controller
        UserSignUpResponse userSignUpResponse = mapper.map(savedUser, UserSignUpResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("Account created successfully",userSignUpResponse)
        );

    }

    @Override
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(UserSignUpRequest userSignUpRequest) {
        boolean isExistByEmail = userRepository.existsByEmail(userSignUpRequest.getEmail());
        boolean isExistByUsername = userRepository.existByUsername(userSignUpRequest.getUsername());
        if (isExistByEmail){
            throw new APIException("Admin with "+userSignUpRequest.getEmail()+ " already exist");
        }
        if (isExistByUsername){
            throw new APIException("Admin with "+userSignUpRequest.getUsername()+ " already exist");
        }
        User newAdmin = mapper.map(userSignUpRequest, User.class);
        newAdmin.setRole(Role.ADMIN);
        newAdmin.setPassword(encoder.encode(userSignUpRequest.getPassword()));
        User savedAdmin = userRepository.save(newAdmin);
        UserSignUpResponse userSignUpResponse = mapper.map(savedAdmin, UserSignUpResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body
                (new ApiResponse<>("Account created successfully",userSignUpResponse));
    }
}
