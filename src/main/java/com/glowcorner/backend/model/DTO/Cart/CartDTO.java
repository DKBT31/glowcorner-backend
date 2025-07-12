package com.glowcorner.backend.model.DTO.Cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CartDTO {

    String userID;

    List<CartItemDTO> items;

    Long totalAmount;
    Long discountedTotalAmount;
}
