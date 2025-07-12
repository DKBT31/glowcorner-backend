package com.glowcorner.backend.model.mapper.Product;

import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.model.DTO.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // Convert Product entity to ProductDTO
    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDTO(
            product.getProductID(),
            product.getProductName(),
            product.getDescription(),
            product.getPrice(),
            product.getDiscountedPrice(),
            product.getSkinTypes(),
            product.getCategory(),
            product.getRating(),
            product.getImage_url(),
            product.getStatus()
        );
    }
}
