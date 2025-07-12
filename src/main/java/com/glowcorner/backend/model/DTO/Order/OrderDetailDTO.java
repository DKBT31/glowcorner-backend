package com.glowcorner.backend.model.DTO.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetailDTO {

    String orderID;
    String productID;
    String productName;
    Integer quantity;
    Long productPrice;
    String discountName;
    Integer discountPercentage;
    Long totalAmount;
    Long discountedTotalAmount;

}