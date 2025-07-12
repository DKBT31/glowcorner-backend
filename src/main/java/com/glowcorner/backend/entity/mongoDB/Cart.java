package com.glowcorner.backend.entity.mongoDB;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "cart") // Maps this entity to the "carts" collection in MongoDB
@CompoundIndex(name = "user_product_unique", def = "{'userID': 1, 'productID': 1}", unique = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
// Ensures a user cannot have duplicate products in their cart
public class Cart {

    @Id
    String id;// Unique MongoDB document identifier

    @Field("userID")
    String userID; // Refers to the userID in the User collection

    List<CartItem> items; // List of products in the cart

    Long totalAmount;
    Long discountedTotalAmount;
}