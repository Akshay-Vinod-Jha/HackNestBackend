package com.hacknest.backend.models.application;

import com.hacknest.backend.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "applications")
@CompoundIndexes({
    @CompoundIndex(name = "team_applicant_idx", def = "{'teamId': 1, 'applicantId': 1}", unique = true)
})
public class Application {

    @Id
    private String id;

    @Indexed
    private String teamId;

    @Indexed
    private String applicantId;

    private String roleApplied;
    private String message;

    @Indexed
    private ApplicationStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
