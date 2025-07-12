package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByEmail(String email);
}