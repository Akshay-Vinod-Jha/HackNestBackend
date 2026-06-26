package com.hacknest.backend.dto.profile;

import com.hacknest.backend.enums.PortfolioType;
import org.hibernate.validator.constraints.URL;
import lombok.Data;

@Data
public class PortfolioLinkDto {
    private PortfolioType type;
    
    @URL(message = "Must be a valid URL format")
    private String url;
}
