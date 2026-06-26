package com.hacknest.backend.controllers.user;

import com.hacknest.backend.dto.common.ApiResponse;
import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.user.UserSummaryResponse;
import com.hacknest.backend.enums.SkillLevel;
import com.hacknest.backend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<UserSummaryResponse>>> searchUsers(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) Integer graduationYear,
            @RequestParam(required = false) Integer profileCompletionMin,
            @RequestParam(required = false) SkillLevel skillLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PagedResponse<UserSummaryResponse> response = userService.searchUsers(
                skills, college, graduationYear, profileCompletionMin, skillLevel, page, size, sortBy, sortDirection);
                
        return ResponseEntity.ok(ApiResponse.success("Users searched successfully", response));
    }
}
