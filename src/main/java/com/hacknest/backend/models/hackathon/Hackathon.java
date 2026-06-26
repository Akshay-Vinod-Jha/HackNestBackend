package com.hacknest.backend.models.hackathon;

import com.hacknest.backend.enums.HackathonMode;
import com.hacknest.backend.enums.HackathonStatus;
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
@Document(collection = "hackathons")
public class Hackathon {

    @Id
    private String id;

    @Indexed
    private String title;

    private String description;
    private String organizer;
    private String websiteUrl;
    private String registrationUrl;
    
    private HackathonMode mode;
    
    @Indexed
    private HackathonStatus status;
    
    private Integer teamSizeMin;
    private Integer teamSizeMax;
    
    @Indexed
    private LocalDateTime registrationDeadline;
    
    private LocalDateTime hackathonStartDate;
    private LocalDateTime hackathonEndDate;
    private String prizePool;
    
    @Indexed
    private String country;
    
    private String city;
    private List<String> domains;
    private List<String> techStacks;
    private List<String> tags;
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
