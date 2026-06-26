package com.hacknest.backend.services.invitation;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.dto.invitation.InvitationSummaryResponse;
import com.hacknest.backend.dto.invitation.SendInvitationRequest;
import com.hacknest.backend.enums.InvitationStatus;

public interface InvitationService {
    InvitationResponse sendInvitation(String teamId, String senderId, SendInvitationRequest request);
    PagedResponse<InvitationSummaryResponse> getMyInvitations(String userId, InvitationStatus status, int page, int size, String sortBy, String sortDirection);
    InvitationResponse acceptInvitation(String invitationId, String userId);
    InvitationResponse rejectInvitation(String invitationId, String userId);
}
