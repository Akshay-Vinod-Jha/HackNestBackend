package com.hacknest.backend.services.hackathon;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;
import com.hacknest.backend.dto.hackathon.HackathonSummaryResponse;

public interface HackathonService {
    HackathonResponse createHackathon(CreateHackathonRequest request, String userId);
    HackathonResponse getHackathonById(String hackathonId);
    PagedResponse<HackathonSummaryResponse> getAllHackathons(int page, int size, String sortBy, String sortDirection);
}
