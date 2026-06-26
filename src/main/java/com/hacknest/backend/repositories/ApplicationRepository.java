package com.hacknest.backend.repositories;

import com.hacknest.backend.models.application.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByTeamId(String teamId);
    List<Application> findByApplicantId(String applicantId);
    boolean existsByTeamIdAndApplicantId(String teamId, String applicantId);
}
