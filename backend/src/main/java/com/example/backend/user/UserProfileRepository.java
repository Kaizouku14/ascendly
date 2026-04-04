package com.example.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@SuppressWarnings("NullableProblems")
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);
}