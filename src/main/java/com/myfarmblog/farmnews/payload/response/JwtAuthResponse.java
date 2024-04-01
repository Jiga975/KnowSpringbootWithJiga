package com.myfarmblog.farmnews.payload.response;

import com.myfarmblog.farmnews.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthResponse {
    Long id;
    String name;
    String email;
    Role role;
    String accessToken;
    String refreshToken;
    String tokenType = "Bearer";
}
