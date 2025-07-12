package com.glowcorner.backend.controller.OrderController;

import com.glowcorner.backend.model.DTO.request.Payment.CreatePaymentIntentRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Stripe Payment System", description = "Stripe payment creation & integration")
@RestController
@RequestMapping("/api/payment/stripe")
public class StripePaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Operation(summary = "Create Stripe PaymentIntent", description = "Creates a payment intent and returns the clientSecret for frontend use")
    @PostMapping("/create-intent")
    public ResponseEntity<ResponseData> createPaymentIntent(@RequestBody CreatePaymentIntentRequest request) {
        try {
            Stripe.apiKey = stripeSecretKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(request.getAmount()) // amount in cents
                    .setCurrency(request.getCurrency()) // e.g., "usd"
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Map<String, String> result = new HashMap<>();
            result.put("clientSecret", intent.getClientSecret());

            return ResponseEntity.ok(new ResponseData(200, true, "PaymentIntent created successfully", result, null, null));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Stripe error: " + e.getMessage(), null, null, null));
        }
    }
}
