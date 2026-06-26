package com.hacknest.backend.repositories;

import com.hacknest.backend.enums.TeamStatus;
import com.hacknest.backend.models.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    Page<Team> findByHackathonId(String hackathonId, Pageable pageable);
    
    List<Team> findByLeaderIdOrMemberIdsContaining(String leaderId, String memberId);
    
    long countByLeaderId(String leaderId);
    long countByMemberIdsContains(String memberId);
    long countByLeaderIdAndStatus(String leaderId, TeamStatus status);
    long countByMemberIdsContainsAndStatus(String memberId, TeamStatus status);
}
