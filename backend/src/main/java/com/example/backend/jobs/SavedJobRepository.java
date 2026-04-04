package com.example.backend.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@SuppressWarnings("NullableProblems")
public interface SavedJobRepository extends JpaRepository<SavedJob, String> {
    List<SavedJob> findByUserId(String userId);
}
