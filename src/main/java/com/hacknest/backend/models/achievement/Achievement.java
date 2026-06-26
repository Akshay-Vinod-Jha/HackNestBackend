package com.hacknest.backend.models.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "achievements")
public class Achievement {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String hackathonId;
    
    private String teamId;
    private String title;
    private String description;
    
    @Indexed
    private AchievementType type;
    
    private Integer position;
    private String certificateUrl;
    private LocalDateTime achievedAt;
}
