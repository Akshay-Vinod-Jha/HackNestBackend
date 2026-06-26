package com.hacknest.backend.repositories;

import com.hacknest.backend.models.hackathon.Hackathon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HackathonRepository extends MongoRepository<Hackathon, String> {
}
