package com.hacknest.backend.controllers.rating;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.rating.CreateRatingRequest;
import com.hacknest.backend.dto.rating.RatingResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.rating.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<ApiResponse<RatingResponse>> createRating(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateRatingRequest request) {
        
        RatingResponse response = ratingService.createRating(request, user.getId());
        return new ResponseEntity<>(ApiResponse.success("Rating submitted successfully", response), HttpStatus.CREATED);
    }
}
