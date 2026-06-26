package com.hacknest.backend.repositories;

import com.hacknest.backend.models.invitation.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends MongoRepository<Invitation, String> {
    List<Invitation> findByReceiverId(String receiverId);
    List<Invitation> findBySenderId(String senderId);
    boolean existsByTeamIdAndReceiverId(String teamId, String receiverId);
}
