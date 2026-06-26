package com.hacknest.backend.repositories;

import com.hacknest.backend.models.achievement.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {
    List<Achievement> findByUserId(String userId);
    long countByUserId(String userId);
    List<Achievement> findByHackathonId(String hackathonId);
}
