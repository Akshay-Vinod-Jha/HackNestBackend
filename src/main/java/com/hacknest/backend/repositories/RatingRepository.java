package com.hacknest.backend.repositories;

import com.hacknest.backend.models.rating.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    
    Page<Rating> findByRatedUserId(String ratedUserId, Pageable pageable);
    
    List<Rating> findByTeamId(String teamId);
    
    boolean existsByRaterIdAndRatedUserIdAndTeamId(String raterId, String ratedUserId, String teamId);
}
