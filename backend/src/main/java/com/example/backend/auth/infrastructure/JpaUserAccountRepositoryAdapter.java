package com.example.backend.auth.infrastructure;

import com.example.backend.auth.application.port.out.UserAccountRepositoryPort;
import com.example.backend.auth.domain.UserAccount;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaUserAccountRepositoryAdapter implements UserAccountRepositoryPort {

    private final UserRepository userRepository;

    public JpaUserAccountRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<UserAccount> findById(UUID userId) {
        return userRepository.findById(userId).map(this::toDomain);
    }

    @Override
    public UserAccount saveNew(String username, String email, String passwordHash) {
        User entity = new User();
        entity.setUsername(username);
        entity.setEmail(email);
        entity.setPassword(passwordHash);

        User saved = userRepository.save(entity);
        return toDomain(saved);
    }

    private UserAccount toDomain(User entity) {
        return new UserAccount(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword());
    }
}
