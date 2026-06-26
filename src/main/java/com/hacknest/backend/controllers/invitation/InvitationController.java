package com.hacknest.backend.controllers.invitation;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.invitation.InvitationResponse;
import com.hacknest.backend.dto.invitation.InvitationSummaryResponse;
import com.hacknest.backend.enums.InvitationStatus;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.invitation.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PagedResponse<InvitationSummaryResponse>>> getMyInvitations(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) InvitationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PagedResponse<InvitationSummaryResponse> response = invitationService.getMyInvitations(
                user.getId(), status, page, size, sortBy, sortDirection);
                
        return ResponseEntity.ok(ApiResponse.success("Invitations fetched successfully", response));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<InvitationResponse>> acceptInvitation(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        
        InvitationResponse response = invitationService.acceptInvitation(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Invitation accepted successfully", response));
    }
}
