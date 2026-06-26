package com.hacknest.backend.controllers.dashboard;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.dashboard.DashboardResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardInsights(@AuthenticationPrincipal User user) {
        DashboardResponse response = dashboardService.getDashboardInsights(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Dashboard insights fetched successfully", response));
    }
}
