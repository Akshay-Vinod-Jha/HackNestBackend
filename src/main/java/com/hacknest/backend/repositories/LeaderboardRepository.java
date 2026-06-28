package com.hacknest.backend.repositories;

import com.hacknest.backend.models.leaderboard.LeaderboardEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderboardRepository extends MongoRepository<LeaderboardEntry, String> {
    Page<LeaderboardEntry> findByCollegeIgnoreCase(String college, Pageable pageable);
    Optional<LeaderboardEntry> findByUserId(String userId);
    long countByTrustScoreGreaterThan(int trustScore);
}
