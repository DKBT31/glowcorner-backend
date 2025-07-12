package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.enums.Category;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.model.DTO.ProductDTO;
import com.glowcorner.backend.model.DTO.request.Product.CreateProductRequest;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(CreateProductRequest request);
    ProductDTO updateProduct(String productId, ProductDTO productDTO);
    void deleteProduct(String id);

    ProductDTO getProductById(String productId);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsBySkinType(SkinType skinType);
    List<ProductDTO> getProductsByCategory(Category category);
    List<ProductDTO> getProductsByFilter(List<SkinType> skinTypes,
                                         List<Category> categories,
                                         Long minPrice,
                                         Long maxPrice);
    List<ProductDTO> getProductsByProductName(String productName);

    // Methods for active products
    List<ProductDTO> getAllActiveProducts();
    ProductDTO getActiveProductById(String productId);
    List<ProductDTO> getActiveProductsBySkinType(SkinType skinType);
    List<ProductDTO> getActiveProductsByCategory(Category category);
    List<ProductDTO> getActiveProductsByProductName(String productName);
    List<ProductDTO> getActiveProductsByFilter(List<SkinType> skinTypes,
                                               List<Category> categories,
                                               Long minPrice,
                                               Long maxPrice);
}
