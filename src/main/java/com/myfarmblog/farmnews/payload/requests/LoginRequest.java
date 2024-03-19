package com.myfarmblog.farmnews.payload.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrEmail;
    private String password;
}
