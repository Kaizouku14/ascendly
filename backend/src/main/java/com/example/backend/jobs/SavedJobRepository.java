package com.example.backend.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("NullableProblems")
@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, String> {
    List<SavedJob> findByUserId(String userId);
}
