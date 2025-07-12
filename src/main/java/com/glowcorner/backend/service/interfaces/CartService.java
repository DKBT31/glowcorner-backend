package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.model.DTO.Cart.CartDTO;
import com.glowcorner.backend.model.DTO.Cart.CartItemDTO;

public interface CartService {

    /* Cart */
    CartDTO getCartByUserID(String userID);

    void addItemToCart(String userID, String productID, int quantity);
    void removeItemFromCart(String userID, String productID);
    void clearCart(String userID);

    /* Cart Item */
    CartItemDTO getCartItem(String userID, String productID);
    void updateCartItem(String userID, String productID, int quantity);
}
