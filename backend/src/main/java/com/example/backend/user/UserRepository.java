package com.example.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@SuppressWarnings("NullableProblems")
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
