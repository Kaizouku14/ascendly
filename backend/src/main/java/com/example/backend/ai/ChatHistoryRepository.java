package com.example.backend.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("NullableProblems")
@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, UUID> {
  List<ChatHistory> findByUserIdOrderByCreatedAtAsc(UUID userId);
}
