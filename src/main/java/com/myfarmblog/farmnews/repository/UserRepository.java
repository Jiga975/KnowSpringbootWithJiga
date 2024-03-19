package com.myfarmblog.farmnews.repository;

import com.myfarmblog.farmnews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User>findByEmail(String email);
    Optional<User>findByUsername(String username);
    Optional<User> findByemailOrUsername(String email, String username);
    Boolean existByUsername(String username);
    Boolean existsByEmail(String email);

}
