package com.hacknest.backend.models.team;

import com.hacknest.backend.enums.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teams")
public class Team {

    @Id
    private String id;

    private String name;
    private String description;
    
    @Indexed
    private String hackathonId;
    
    @Indexed
    private String leaderId;
    
    private List<String> memberIds;
    private List<String> requiredRoles;
    private List<String> requiredSkills;
    
    private Integer maxMembers;
    private Integer currentMemberCount;
    
    @Indexed
    private Boolean isOpen;
    
    @Indexed
    private TeamStatus status;
    
    private Integer teamCompletionPercentage;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
