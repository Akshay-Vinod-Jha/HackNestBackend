package com.hacknest.backend.models.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ratings")
@CompoundIndex(def = "{'raterId': 1, 'ratedUserId': 1, 'teamId': 1}", unique = true)
public class Rating {

    @Id
    private String id;

    private String hackathonId;

    @Indexed
    private String teamId;

    @Indexed
    private String raterId;

    @Indexed
    private String ratedUserId;

    private List<SkillRating> skillRatings;
    
    private Integer reliabilityRating;
    
    private Integer contributionRating;
    
    private String comment;

    @CreatedDate
    private LocalDateTime createdAt;
}
