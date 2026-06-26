package com.hacknest.backend.dto.trust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrustScoreResponse {
    private String userId;
    private int trustScore;
    private List<TrustReason> reasons;
}
