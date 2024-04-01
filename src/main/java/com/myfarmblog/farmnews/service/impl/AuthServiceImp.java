package com.myfarmblog.farmnews.service.impl;

import com.myfarmblog.farmnews.entity.enums.Role;
import com.myfarmblog.farmnews.entity.model.User;
import com.myfarmblog.farmnews.events.publishers.EventPublisher;
import com.myfarmblog.farmnews.exceptions.APIException;
import com.myfarmblog.farmnews.payload.requests.LoginRequest;
import com.myfarmblog.farmnews.payload.requests.UserSignUpRequest;
import com.myfarmblog.farmnews.payload.response.ApiResponse;
import com.myfarmblog.farmnews.payload.response.JwtAuthResponse;
import com.myfarmblog.farmnews.payload.response.UserSignUpResponse;
import com.myfarmblog.farmnews.repository.UserRepository;
import com.myfarmblog.farmnews.securitty.JwtTokenProvider;
import com.myfarmblog.farmnews.service.AuthService;
import com.myfarmblog.farmnews.utils.HelperClass;
import com.myfarmblog.farmnews.utils.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImp implements AuthService {
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final HelperClass helperClass;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EventPublisher publisher;
    private final HttpServletRequest request;

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
        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedUser.getEmail(),savedUser.getName(),request);

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
        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedAdmin.getEmail(),savedAdmin.getName(),request);
        UserSignUpResponse userSignUpResponse = mapper.map(savedAdmin, UserSignUpResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body
                (new ApiResponse<>("Account created successfully",userSignUpResponse));
    }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(LoginRequest loginRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>("user not found")
            );
        }
        String decryptedPassword = helperClass.decryptPassword(loginRequest.getPassword());

        // Authentication manager to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), decryptedPassword)
        );
        //write code to verify user and confirm verified users before allowing login

        //generated jwt token
        String token = jwtTokenProvider.generateToken(authentication, SecurityConstants.JWT_SIGNUP_EXPIRATION);

        //generate refresh token
        String refreshToken = jwtTokenProvider.generateToken(authentication,SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION);

        // Save authentication in security context so user won't have to login everytime the network is called
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userOptional.get();
        JwtAuthResponse authResponse = JwtAuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new ApiResponse<>("login successful", authResponse));

    }

    @Override
    public void logout() {
        //to logout just clear the details retained in security context
        SecurityContextHolder.clearContext();
    }
}
