package com.hacknest.backend.services.profile;

import com.hacknest.backend.dto.profile.ProfileResponse;
import com.hacknest.backend.dto.profile.UpdateProfileRequest;

public interface ProfileService {
    ProfileResponse getMyProfile(String userId);
    ProfileResponse updateMyProfile(String userId, UpdateProfileRequest request);
}
