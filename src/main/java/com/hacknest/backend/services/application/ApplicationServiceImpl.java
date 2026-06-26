package com.hacknest.backend.services.application;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.application.ApplicationSummaryResponse;
import com.hacknest.backend.dto.application.ApplyRequest;
import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.enums.ApplicationStatus;
import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.models.application.Application;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.ApplicationRepository;
import com.hacknest.backend.repositories.TeamRepository;
import com.hacknest.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public ApplicationResponse applyToTeam(String teamId, String userId, ApplyRequest request) {
        
        // 1. Team must exist
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        // 2. Team must be open
        if (!team.getIsOpen() || team.getStatus() == TeamStatus.FULL || team.getStatus() == TeamStatus.COMPLETED) {
            throw new IllegalArgumentException("Team is not open for applications");
        }

        // 3. User cannot apply to own team (or a team they are already in)
        if (team.getMemberIds().contains(userId)) {
            throw new IllegalArgumentException("You are already a member of this team");
        }

        // 4. User cannot apply twice
        if (applicationRepository.existsByTeamIdAndApplicantId(teamId, userId)) {
            throw new IllegalArgumentException("You have already applied to this team");
        }

        // 5. Team should not already be full
        if (team.getCurrentMemberCount() >= team.getMaxMembers()) {
            throw new IllegalArgumentException("Team is already full");
        }

        // 6. Requested role must exist
        Optional<RequiredRole> requestedRoleOpt = team.getRequiredRoles().stream()
                .filter(role -> role.getRoleName().equalsIgnoreCase(request.getRoleApplied()))
                .findFirst();

        if (requestedRoleOpt.isEmpty()) {
            throw new IllegalArgumentException("Requested role does not exist for this team");
        }

        // 7. Requested role should still have available slots
        RequiredRole requestedRole = requestedRoleOpt.get();
        if (requestedRole.getFilledSlots() >= requestedRole.getSlots()) {
            throw new IllegalArgumentException("Requested role is already filled");
        }

        Application application = Application.builder()
                .teamId(teamId)
                .applicantId(userId)
                .roleApplied(request.getRoleApplied())
                .message(request.getMessage())
                .status(ApplicationStatus.PENDING)
                .build();

        application = applicationRepository.save(application);

        return ApplicationResponse.builder()
                .id(application.getId())
                .teamId(application.getTeamId())
                .applicantId(application.getApplicantId())
                .roleApplied(application.getRoleApplied())
                .message(application.getMessage())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    @Override
    public PagedResponse<ApplicationSummaryResponse> getTeamApplications(String teamId, String userId, ApplicationStatus status, int page, int size, String sortBy, String sortDirection) {
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
                
        if (!team.getLeaderId().equals(userId)) {
            throw new IllegalArgumentException("Only the team leader can view applications");
        }
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Application> appPage;
        if (status != null) {
            appPage = applicationRepository.findByTeamIdAndStatus(teamId, status, pageable);
        } else {
            appPage = applicationRepository.findByTeamId(teamId, pageable);
        }
        
        Page<ApplicationSummaryResponse> summaryPage = appPage.map(app -> {
            String applicantName = userRepository.findById(app.getApplicantId())
                    .map(user -> user.getFullName())
                    .orElse("Unknown User");
                    
            return ApplicationSummaryResponse.builder()
                    .id(app.getId())
                    .roleApplied(app.getRoleApplied())
                    .status(app.getStatus())
                    .createdAt(app.getCreatedAt())
                    .applicantId(app.getApplicantId())
                    .applicantName(applicantName)
                    .build();
        });
        
        return PagedResponse.of(summaryPage);
    }
}
