package com.glowcorner.backend.model.mapper.Cart;

import com.glowcorner.backend.entity.mongoDB.Cart;
import com.glowcorner.backend.model.DTO.Cart.CartDTO;
import org.springframework.stereotype.Component;


@Component
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public CartDTO toCartDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        return new CartDTO(
                cart.getUserID(),
                cart.getItems().stream()
                        .map(cartItemMapper::toCartItemDTO)
                        .collect(java.util.stream.Collectors.toList()),
                cart.getTotalAmount(),
                cart.getDiscountedTotalAmount()
        );
    }

}
