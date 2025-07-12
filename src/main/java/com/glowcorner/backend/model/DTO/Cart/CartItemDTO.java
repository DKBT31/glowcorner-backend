package com.glowcorner.backend.model.DTO.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CartItemDTO {

    String userID;
    String productID;
    String productName;
    Long productPrice;
    int quantity;
    Long totalAmount;

    Integer discountPercentage;
    Long discountedTotalAmount;

}
