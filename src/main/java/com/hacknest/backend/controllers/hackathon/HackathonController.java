package com.hacknest.backend.controllers.hackathon;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.hackathon.HackathonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hackathons")
@RequiredArgsConstructor
public class HackathonController {

    private final HackathonService hackathonService;

    @PostMapping
    public ResponseEntity<ApiResponse<HackathonResponse>> createHackathon(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateHackathonRequest request) {
        
        HackathonResponse response = hackathonService.createHackathon(request, user.getId());
        return new ResponseEntity<>(ApiResponse.success("Hackathon created successfully", response), HttpStatus.CREATED);
    }
}
