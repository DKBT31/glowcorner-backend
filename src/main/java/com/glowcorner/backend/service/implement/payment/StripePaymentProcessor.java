package com.glowcorner.backend.service.implement.payment;

import com.glowcorner.backend.entity.mongoDB.Order;
import com.glowcorner.backend.enums.Status.OrderStatus;
import com.glowcorner.backend.model.DTO.Order.PaymentInfo;
import com.glowcorner.backend.service.interfaces.payment.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(Order order, PaymentInfo info) {
        try {
            String intentId = info.getPaymentIntentId();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(intentId);
            PaymentMethod method = PaymentMethod.retrieve(paymentIntent.getPaymentMethod());

            order.setPaymentIntentId(intentId);
            order.setPaymentMethodType(method.getType());
            order.setPaymentBrand(method.getCard().getBrand());
            order.setPaymentLast4(method.getCard().getLast4());
            order.setStatus(OrderStatus.PROCESSING);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage());
        }
    }

    @Override
    public com.glowcorner.backend.enums.PaymentMethod getSupportedMethod() {
        return com.glowcorner.backend.enums.PaymentMethod.STRIPE;
    }

}

