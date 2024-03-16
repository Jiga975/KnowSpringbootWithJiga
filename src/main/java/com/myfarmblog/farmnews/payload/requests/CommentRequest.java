package com.myfarmblog.farmnews.payload.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long id;
    @NotEmpty
    @Size(min =6, message = "Name must be upto 6 characters")
    private String name;
    @NotEmpty
    @Email(message = "must follow email format")
    private String email;
    private String body;
}
