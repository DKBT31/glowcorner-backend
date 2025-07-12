package com.glowcorner.backend.service.implement.payment;

import com.glowcorner.backend.entity.mongoDB.Order;
import com.glowcorner.backend.enums.PaymentMethod;
import com.glowcorner.backend.enums.Status.OrderStatus;
import com.glowcorner.backend.model.DTO.Order.PaymentInfo;
import com.glowcorner.backend.service.interfaces.payment.PaymentProcessor;
import org.springframework.stereotype.Service;

@Service
public class CodPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(Order order, PaymentInfo info) {
        order.setPaymentMethodType("cash");
        order.setPaymentBrand("Cash on Delivery");
        order.setPaymentLast4(null);
        order.setStatus(OrderStatus.PROCESSING);
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }

}

