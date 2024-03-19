package com.myfarmblog.farmnews.payload.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {
    private String name;
    private String username;
    private String email;
    private String password;
}
