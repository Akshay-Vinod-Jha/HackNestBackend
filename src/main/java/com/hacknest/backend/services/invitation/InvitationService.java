package com.hacknest.backend.services.invitation;

import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.dto.invitation.SendInvitationRequest;

public interface InvitationService {
    InvitationResponse sendInvitation(String teamId, String senderId, SendInvitationRequest request);
}
