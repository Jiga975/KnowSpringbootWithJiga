package com.myfarmblog.farmnews.config;

import com.myfarmblog.farmnews.securitty.CustomUserDetailsService;
import com.myfarmblog.farmnews.securitty.JwtAuthenticationEntryPoint;
import com.myfarmblog.farmnews.securitty.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableAsync
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //password encryption
    }



    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        // Execute JwtAuthenticationFilter before the UsernamePasswordAuthenticationFilter of spring security
        httpSecurity .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.csrf(CsrfConfigurer::disable) //the csrf is enabled automatically for security reasons, disable it while customizing
                .authorizeHttpRequests(
                        requests->requests
                                .requestMatchers(HttpMethod.POST,"api/auth/**").permitAll()
                                //state the link to the request you wish to grant access
                                .requestMatchers(antMatcher("/v3/api-docs/**"),
                                        antMatcher("/swagger-ui/**"),
                                        antMatcher("/swagger-resources/**"),
                                        antMatcher("/swagger-ui.html/**"),
                                        antMatcher("/webjars/**")).permitAll()
                                //permit all requests coming from the swagger link above
                                .anyRequest()
                                .authenticated())//authenticate anyRequest except the one explicitly stated above
                                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(authenticationEntryPoint))
                                .sessionManagement(session->session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
