package com.hacknest.backend.services.rating;

import com.hacknest.backend.dto.rating.CreateRatingRequest;
import com.hacknest.backend.dto.rating.RatingResponse;

public interface RatingService {
    RatingResponse createRating(CreateRatingRequest request, String raterId);
}
