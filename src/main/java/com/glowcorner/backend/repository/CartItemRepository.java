package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends MongoRepository<CartItem, String> {
    List<CartItem> findCartItemsByUserID(String userID);

    Optional<CartItem> findCartItemByUserIDAndProductID(String userID, String productID);

    void deleteCartItemByUserID(String userID);
}
