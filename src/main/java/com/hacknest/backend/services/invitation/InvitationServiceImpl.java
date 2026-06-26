package com.hacknest.backend.services.invitation;

import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.dto.invitation.SendInvitationRequest;
import com.hacknest.backend.enums.InvitationStatus;
import com.hacknest.backend.models.invitation.Invitation;
import com.hacknest.backend.models.team.RequiredRole;
import com.hacknest.backend.models.team.Team;
import com.hacknest.backend.repositories.InvitationRepository;
import com.hacknest.backend.repositories.TeamRepository;
import com.hacknest.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public InvitationResponse sendInvitation(String teamId, String senderId, SendInvitationRequest request) {
        
        // 1. Team must exist
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        // 2. Sender must be team leader
        if (!team.getLeaderId().equals(senderId)) {
            throw new IllegalArgumentException("Only the team leader can send invitations");
        }

        // 3. Receiver profile must exist
        if (!userRepository.existsById(request.getReceiverId())) {
            throw new IllegalArgumentException("Receiver profile not found");
        }

        // 4. Team should not be full
        if (team.getCurrentMemberCount() >= team.getMaxMembers()) {
            throw new IllegalArgumentException("Team is already full");
        }

        // 5. Receiver should not already be team member
        if (team.getMemberIds().contains(request.getReceiverId())) {
            throw new IllegalArgumentException("Receiver is already a member of this team");
        }

        // 6. Receiver should not already have pending invitation
        if (invitationRepository.existsByTeamIdAndReceiverId(teamId, request.getReceiverId())) {
            throw new IllegalArgumentException("Receiver already has an invitation for this team");
        }

        // 7. Offered role must exist
        Optional<RequiredRole> offeredRoleOpt = team.getRequiredRoles().stream()
                .filter(role -> role.getRoleName().equalsIgnoreCase(request.getRoleOffered()))
                .findFirst();

        if (offeredRoleOpt.isEmpty()) {
            throw new IllegalArgumentException("Offered role does not exist in this team");
        }

        // 8. Offered role should still have available slots
        RequiredRole offeredRole = offeredRoleOpt.get();
        if (offeredRole.getFilledSlots() >= offeredRole.getSlots()) {
            throw new IllegalArgumentException("Offered role is already filled");
        }

        Invitation invitation = Invitation.builder()
                .teamId(teamId)
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .roleOffered(request.getRoleOffered())
                .message(request.getMessage())
                .status(InvitationStatus.PENDING)
                .build();

        invitation = invitationRepository.save(invitation);

        return InvitationResponse.builder()
                .id(invitation.getId())
                .teamId(invitation.getTeamId())
                .senderId(invitation.getSenderId())
                .receiverId(invitation.getReceiverId())
                .roleOffered(invitation.getRoleOffered())
                .message(invitation.getMessage())
                .status(invitation.getStatus())
                .createdAt(invitation.getCreatedAt())
                .updatedAt(invitation.getUpdatedAt())
                .build();
    }
}
