package com.hacknest.backend.services.dashboard;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.dashboard.DashboardResponse;
import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.enums.ApplicationStatus;
import com.hacknest.backend.enums.InvitationStatus;
import com.hacknest.backend.models.User;
import com.hacknest.backend.models.application.Application;
import com.hacknest.backend.models.invitation.Invitation;
import com.hacknest.backend.repositories.ApplicationRepository;
import com.hacknest.backend.repositories.InvitationRepository;
import com.hacknest.backend.repositories.UserRepository;
import com.hacknest.backend.services.profile.ProfileService;
import com.hacknest.backend.services.recommendation.RecommendationService;
import com.hacknest.backend.services.trust.TrustService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final RecommendationService recommendationService;
    private final ProfileService profileService;
    private final TrustService trustService;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final InvitationRepository invitationRepository;

    @Override
    public DashboardResponse getDashboardInsights(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        int profileCompletion = user.getProfile() != null ? user.getProfile().getProfileCompletionPercentage() : 0;
        int trustScore = trustService.calculateTrustScore(userId).getTrustScore();
        
        List<Application> allApplications = applicationRepository.findByApplicantId(userId);
        List<ApplicationResponse> pendingApplications = allApplications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                .map(this::mapApplication)
                .collect(Collectors.toList());

        List<Invitation> allInvitations = invitationRepository.findByReceiverId(userId);
        List<InvitationResponse> pendingInvitations = allInvitations.stream()
                .filter(i -> i.getStatus() == InvitationStatus.PENDING)
                .map(this::mapInvitation)
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .recommendedTeams(recommendationService.getTeamRecommendations(userId))
                .recommendedHackathons(recommendationService.getHackathonRecommendations(userId))
                .pendingApplications(pendingApplications)
                .pendingInvitations(pendingInvitations)
                .profileCompletionPercentage(profileCompletion)
                .trustScore(trustScore)
                .analyticsSummary(profileService.getAnalytics(userId))
                .build();
    }
    
    private ApplicationResponse mapApplication(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .teamId(app.getTeamId())
                .applicantId(app.getApplicantId())
                .roleApplied(app.getRoleApplied())
                .message(app.getMessage())
                .status(app.getStatus())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
    
    private InvitationResponse mapInvitation(Invitation inv) {
        return InvitationResponse.builder()
                .id(inv.getId())
                .teamId(inv.getTeamId())
                .senderId(inv.getSenderId())
                .receiverId(inv.getReceiverId())
                .roleOffered(inv.getRoleOffered())
                .message(inv.getMessage())
                .status(inv.getStatus())
                .createdAt(inv.getCreatedAt())
                .updatedAt(inv.getUpdatedAt())
                .build();
    }
}
