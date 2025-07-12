package com.glowcorner.backend.model.DTO.Order;

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
public class OrderDTO {

    String orderID;
    String customerID;
    String customerName;
    LocalDate orderDate;
    OrderStatus status;
    Long totalAmount;
    Long discountedTotalAmount;
    List<OrderDetailDTO> orderDetails;

    String paymentIntentId;

    String paymentMethodType;
    String paymentBrand;
    String paymentLast4;

    String stripePaymentMethodId;

}
