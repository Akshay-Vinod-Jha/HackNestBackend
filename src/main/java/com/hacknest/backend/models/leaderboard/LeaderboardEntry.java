package com.hacknest.backend.models.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leaderboard")
public class LeaderboardEntry {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String userId;
    
    private String fullName;
    
    @Indexed
    private String college;
    
    @Indexed
    private int trustScore;
    
    @Indexed
    private int achievements;
    
    @Indexed
    private int participation;
    
    @Indexed
    private int contribution;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
