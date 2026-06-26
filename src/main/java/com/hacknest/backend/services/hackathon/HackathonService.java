package com.hacknest.backend.services.hackathon;

import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;

public interface HackathonService {
    HackathonResponse createHackathon(CreateHackathonRequest request, String userId);
    HackathonResponse getHackathonById(String hackathonId);
}
