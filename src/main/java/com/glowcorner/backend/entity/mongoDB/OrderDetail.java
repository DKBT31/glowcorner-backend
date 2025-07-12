package com.glowcorner.backend.entity.mongoDB;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "orderDetail") // Maps this class to the "orderDetails" collection in MongoDB
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@CompoundIndex(name = "order_product_unique", def = "{'orderID': 1, 'productID': 1}", unique = true)
// Enforces the uniqueness of the combination of orderID and productID
public class OrderDetail {

    @Id
    String id; // Unique MongoDB document identifier (can be ignored if unused)

    String orderID; // Refers to the orderID in the Order collection

    String productID; // Refers to the productID in the Product collection

    String productName;

    Long productPrice;

    String discountName;

    Integer discountPercentage;

    int quantity;

    Long totalAmount;

    Long discountedTotalAmount;
}