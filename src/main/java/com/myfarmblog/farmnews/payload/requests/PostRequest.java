package com.myfarmblog.farmnews.payload.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long id;
    @NotEmpty //post tittle should not be null or empty
    @Size(min = 3, message = "Post tittle should have at least 3 characters")
    private String title;
    @NotEmpty //post tittle should not be null or empty
    @Size(min = 15, message = "Post tittle should have at least 15 characters")
    private String description;
    @NotEmpty //post tittle should not be null or empty
    private String content;
    private Set<CommentRequest>comments;
}
