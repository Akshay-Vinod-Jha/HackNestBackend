package com.hacknest.backend.models.invitation;

import com.hacknest.backend.enums.InvitationStatus;
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
@Document(collection = "invitations")
@CompoundIndexes({
    @CompoundIndex(name = "team_receiver_idx", def = "{'teamId': 1, 'receiverId': 1}", unique = true)
})
public class Invitation {

    @Id
    private String id;

    @Indexed
    private String teamId;

    @Indexed
    private String senderId;

    @Indexed
    private String receiverId;

    private String roleOffered;
    private String message;

    @Indexed
    private InvitationStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
