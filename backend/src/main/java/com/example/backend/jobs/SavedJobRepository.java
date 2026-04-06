package com.example.backend.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, UUID> {
    List<SavedJob> findByUserId(UUID userId);
}
