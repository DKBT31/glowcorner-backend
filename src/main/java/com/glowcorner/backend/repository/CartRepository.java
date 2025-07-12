package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserID(String userID);
    void deleteCartByUserID(String userID);
}
