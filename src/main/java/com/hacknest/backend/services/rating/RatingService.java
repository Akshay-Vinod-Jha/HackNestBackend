package com.hacknest.backend.services.rating;

import com.hacknest.backend.dto.rating.CreateRatingRequest;
import com.hacknest.backend.dto.rating.RatingResponse;
import com.hacknest.backend.dto.rating.UserRatingSummaryResponse;
import java.util.Map;

public interface RatingService {
    RatingResponse createRating(CreateRatingRequest request, String raterId);
    UserRatingSummaryResponse getUserRatingSummary(String userId);
    Map<String, Double> calculateSkillRatings(String userId);
}
