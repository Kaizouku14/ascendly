package com.example.backend.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("NullableProblems")
@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, String> {
  List<ChatHistory> findByUserIdOrderByCreatedAtAsc(String userId);
}
