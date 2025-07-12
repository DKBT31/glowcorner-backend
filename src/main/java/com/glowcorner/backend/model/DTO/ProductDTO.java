package com.glowcorner.backend.model.DTO;

import com.glowcorner.backend.enums.Category;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Status.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDTO {

    String productID;
    String productName;
    String description;
    Long price;
    Long discountedPrice;
    List<SkinType> skinTypes;
    Category category;
    Float rating;
    String image_url;
    ProductStatus status;
}