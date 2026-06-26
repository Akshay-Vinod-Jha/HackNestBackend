package com.hacknest.backend.repositories;

import com.hacknest.backend.enums.InvitationStatus;
import com.hacknest.backend.models.invitation.Invitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends MongoRepository<Invitation, String> {
    List<Invitation> findByReceiverId(String receiverId);
    Page<Invitation> findByReceiverId(String receiverId, Pageable pageable);
    Page<Invitation> findByReceiverIdAndStatus(String receiverId, InvitationStatus status, Pageable pageable);
    List<Invitation> findBySenderId(String senderId);
    boolean existsByTeamIdAndReceiverId(String teamId, String receiverId);
}
