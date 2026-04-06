package com.example.backend.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();
}
