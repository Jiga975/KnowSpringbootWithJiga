package com.myfarmblog.farmnews.payload.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    @Size(min = 3, max = 25, message = "Name is too long or short")
    @NotBlank(message = "First name cannot be blank")
    private String name;

    @Size(min = 3, max = 25, message = "Username is too long or short")
    @NotBlank(message = "cannot be blank")
    private String username;

    @Size(max = 35)
    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email should not be blank!")
    private String email;

    @Size(min = 4, max = 15, message = "Password too short or long")
    @NotBlank(message = "Password cannot be blank!")
    private String password;
}
