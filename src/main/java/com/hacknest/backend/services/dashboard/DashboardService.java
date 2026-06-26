package com.hacknest.backend.services.dashboard;

import com.hacknest.backend.dto.dashboard.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboardInsights(String userId);
}
