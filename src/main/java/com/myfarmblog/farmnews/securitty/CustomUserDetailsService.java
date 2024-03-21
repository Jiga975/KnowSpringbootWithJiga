package com.myfarmblog.farmnews.securitty;

import com.myfarmblog.farmnews.entity.Role;
import com.myfarmblog.farmnews.entity.User;
import com.myfarmblog.farmnews.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public static UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByemailOrUsername(usernameOrEmail,usernameOrEmail)
                .orElseThrow(()->new UsernameNotFoundException("user with: "+usernameOrEmail+" not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                rolesToAuthorities(user.getRoles())
        );
    }
//created a method that converts roles in the entity to granted authorities
    private Collection<? extends GrantedAuthority> rolesToAuthorities(Set<Role>roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
