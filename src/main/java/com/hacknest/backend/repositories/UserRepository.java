package com.hacknest.backend.repositories;

import com.hacknest.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
    
    boolean existsByEmail(String email);
}
