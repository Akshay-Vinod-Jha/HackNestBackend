package com.hacknest.backend.models.profile;

import com.hacknest.backend.enums.PortfolioType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioLink {
    private PortfolioType type;
    private String url;
}
