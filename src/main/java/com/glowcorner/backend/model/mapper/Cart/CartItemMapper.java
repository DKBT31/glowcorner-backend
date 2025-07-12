package com.glowcorner.backend.model.mapper.Cart;

import com.glowcorner.backend.entity.mongoDB.CartItem;
import com.glowcorner.backend.model.DTO.Cart.CartItemDTO;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemDTO toCartItemDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setUserID(cartItem.getUserID());
        cartItemDTO.setProductID(cartItem.getProductID());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setTotalAmount(cartItem.getTotalAmount());
        cartItemDTO.setDiscountedTotalAmount(cartItem.getDiscountedTotalAmount());
        cartItemDTO.setProductPrice(cartItem.getProductPrice());
        cartItemDTO.setDiscountPercentage(cartItem.getDiscountPercentage());
        return cartItemDTO;
    }

    public CartItem toCartItem(CartItemDTO cartItemDTO) {
        if (cartItemDTO == null) {
            return null;
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserID(cartItemDTO.getUserID());
        cartItem.setProductID(cartItemDTO.getProductID());
        cartItem.setProductName(cartItemDTO.getProductName());
        cartItem.setProductPrice(cartItemDTO.getProductPrice());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setTotalAmount(cartItemDTO.getTotalAmount());
        cartItem.setDiscountPercentage(cartItemDTO.getDiscountPercentage());
        cartItem.setDiscountedTotalAmount(cartItemDTO.getDiscountedTotalAmount());

        return cartItem;
    }

}
