package com.hacknest.backend.dto.trust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrustReason {
    private String reason;
    private int scoreImpact;
}
