package com.hacknest.backend.services.trust;

import com.hacknest.backend.enums.ApplicationStatus;
import com.hacknest.backend.enums.InvitationStatus;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.rating.Rating;
import com.hacknest.backend.repositories.ApplicationRepository;
import com.hacknest.backend.repositories.InvitationRepository;
import com.hacknest.backend.repositories.RatingRepository;
import com.hacknest.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrustServiceImpl implements TrustService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final InvitationRepository invitationRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public int calculateReliabilityScore(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 1. Profile Completion (Max 20 points)
        double completionScore = 0;
        if (user.getProfile() != null && user.getProfile().getProfileCompletionPercentage() != null) {
            completionScore = user.getProfile().getProfileCompletionPercentage() * 0.2; // 100% -> 20 pts
        }

        // 2. Successful Engagements (Max 30 points)
        long acceptedInvitations = invitationRepository.countByReceiverIdAndStatus(userId, InvitationStatus.ACCEPTED);
        long acceptedApplications = applicationRepository.countByApplicantIdAndStatus(userId, ApplicationStatus.ACCEPTED);
        
        long totalEngagements = acceptedInvitations + acceptedApplications;
        double engagementScore = Math.min(totalEngagements * 5.0, 30.0); // 6 successful engagements to max out

        // 3. Peer Reliability Rating (Max 50 points)
        List<Rating> ratings = ratingRepository.findByRatedUserId(userId);
        double peerScore = 25.0; // Default neutral baseline
        
        if (ratings != null && !ratings.isEmpty()) {
            double sumReliability = 0;
            int count = 0;
            for (Rating r : ratings) {
                if (r.getReliabilityRating() != null) {
                    sumReliability += r.getReliabilityRating();
                    count++;
                }
            }
            if (count > 0) {
                double avgReliability = sumReliability / count;
                peerScore = (avgReliability / 5.0) * 50.0; // 5.0 avg -> 50 pts
            }
        }

        int reliabilityScore = (int) Math.round(completionScore + engagementScore + peerScore);
        
        // Cap at 100 max, floor at 0 min
        return Math.max(0, Math.min(reliabilityScore, 100));
    }
}
