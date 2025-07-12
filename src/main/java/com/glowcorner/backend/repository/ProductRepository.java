package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.enums.Category;
import com.glowcorner.backend.enums.SkinType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsById(String productID);

    Optional<Product> findByProductID(String productId);

    void deleteByProductID(String productID);
    List<Product> findBySkinTypesIn(SkinType skinType);
    List<Product> findByCategory(Category category);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
}
