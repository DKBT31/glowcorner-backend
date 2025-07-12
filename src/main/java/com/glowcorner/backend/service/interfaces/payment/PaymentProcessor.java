package com.glowcorner.backend.service.interfaces.payment;

import com.glowcorner.backend.entity.mongoDB.Order;
import com.glowcorner.backend.enums.PaymentMethod;
import com.glowcorner.backend.model.DTO.Order.PaymentInfo;

public interface PaymentProcessor {
    void processPayment(Order order, PaymentInfo paymentInfo);
    PaymentMethod getSupportedMethod();
}

