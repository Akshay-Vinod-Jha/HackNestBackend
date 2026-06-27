package com.hacknest.backend.controllers.application;

import com.hacknest.backend.dto.application.ApplicationResponse;
import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getMyApplications(
            @AuthenticationPrincipal User user) {
        
        List<ApplicationResponse> response = applicationService.getUserApplications(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Applications fetched successfully", response));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<ApplicationResponse>> acceptApplication(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        
        ApplicationResponse response = applicationService.acceptApplication(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Application accepted successfully", response));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ApplicationResponse>> rejectApplication(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        
        ApplicationResponse response = applicationService.rejectApplication(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Application rejected successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        
        applicationService.withdrawApplication(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn successfully", null));
    }
}
