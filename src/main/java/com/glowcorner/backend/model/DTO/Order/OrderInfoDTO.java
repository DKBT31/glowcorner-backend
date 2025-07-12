package com.glowcorner.backend.model.DTO.Order;

import com.glowcorner.backend.enums.PaymentMethod;
import com.glowcorner.backend.enums.Status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderInfoDTO {

    String orderID;

    CustomerInfoDTO customerInfo;

    LocalDate orderDate;

    OrderStatus status;

    PaymentMethod paymentMethodType;

    List<OrderDetailItemDTO> orderDetails;

    Long totalAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class CustomerInfoDTO {
        String name;
        String email;
        String phone;
        String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class OrderDetailItemDTO {
        String productID;
        String name;
        Long productPrice;
        Integer discount;
        Integer quantity;
        String image;

    }
}