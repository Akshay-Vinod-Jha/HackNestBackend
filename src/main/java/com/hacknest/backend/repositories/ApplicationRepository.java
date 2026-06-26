package com.hacknest.backend.repositories;

import com.hacknest.backend.enums.ApplicationStatus;
import com.hacknest.backend.models.application.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByTeamId(String teamId);
    Page<Application> findByTeamId(String teamId, Pageable pageable);
    Page<Application> findByTeamIdAndStatus(String teamId, ApplicationStatus status, Pageable pageable);
    List<Application> findByApplicantId(String applicantId);
    boolean existsByTeamIdAndApplicantId(String teamId, String applicantId);
}
