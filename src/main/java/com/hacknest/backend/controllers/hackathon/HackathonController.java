package com.hacknest.backend.controllers.hackathon;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;
import com.hacknest.backend.dto.hackathon.HackathonSummaryResponse;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.hackathon.HackathonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HackathonResponse>> getHackathonById(@PathVariable String id) {
        HackathonResponse response = hackathonService.getHackathonById(id);
        return ResponseEntity.ok(ApiResponse.success("Hackathon fetched successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<HackathonSummaryResponse>>> getAllHackathons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PagedResponse<HackathonSummaryResponse> response = hackathonService.getAllHackathons(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success("Hackathons fetched successfully", response));
    }
}
