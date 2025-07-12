package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserID(String userID);

    List<User> findByFullNameContainingIgnoreCase(String fullName);

    void deleteUserByUserID(String userID);
}
