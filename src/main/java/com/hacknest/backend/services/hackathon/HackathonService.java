package com.hacknest.backend.services.hackathon;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;
import com.hacknest.backend.dto.hackathon.HackathonSummaryResponse;

import java.time.LocalDateTime;

public interface HackathonService {
    HackathonResponse createHackathon(CreateHackathonRequest request, String userId);
    HackathonResponse getHackathonById(String hackathonId);
    PagedResponse<HackathonSummaryResponse> getAllHackathons(int page, int size, String sortBy, String sortDirection);
    PagedResponse<HackathonSummaryResponse> searchHackathons(String country, String mode, String status, String domain, String techStack, String tag, LocalDateTime registrationDeadlineBefore, int page, int size, String sortBy, String sortDirection);
}
