package com.myfarmblog.farmnews.securitty;

import com.myfarmblog.farmnews.entity.model.Role;
import com.myfarmblog.farmnews.entity.model.User;
import com.myfarmblog.farmnews.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByemailOrUsername(usernameOrEmail,usernameOrEmail)
                .orElseThrow(()->new UsernameNotFoundException("user with: "+usernameOrEmail+" not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                rolesToAuthorities(user)
         );

    }
    public Collection<GrantedAuthority> rolesToAuthorities(User user){
        return Collections.singleton(new SimpleGrantedAuthority(String.valueOf(user.getRole())));
    }
}
