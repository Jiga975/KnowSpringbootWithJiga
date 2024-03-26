package com.myfarmblog.farmnews.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
}
