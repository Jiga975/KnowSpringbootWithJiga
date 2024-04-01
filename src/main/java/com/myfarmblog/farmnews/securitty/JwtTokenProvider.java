package com.myfarmblog.farmnews.securitty;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.ES256);
    private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private String generateNewToken(String username, Long milliSeconds){

        log.info("User with username: {} logged in", username);

        Date currentDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + milliSeconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(jwtKey,SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateToken(Authentication authentication, Long milliSeconds) {
        String email = authentication.getName();

        return generateNewToken(email,milliSeconds);
    }

    //getting username from token
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT Token");
            throw new JwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token");
            throw new JwtException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token");
            throw new JwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT Claims string is empty");
            throw new JwtException("JWT Claims string is empty");
        }
    }

    public String generateSignUpVerificationToken(String email, Long milliSeconds){
        Date currentDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + milliSeconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(jwtKey,SignatureAlgorithm.HS512)
                .compact();
    }
}



