package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.Authentication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends MongoRepository<Authentication, String> {
    Optional<Authentication> findByUsername(String username);
    Optional<Authentication> findByUserID(String userID);
    void deleteByUserID(String userID);
}
