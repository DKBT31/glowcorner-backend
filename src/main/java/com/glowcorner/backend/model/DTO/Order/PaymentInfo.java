package com.glowcorner.backend.model.DTO.Order;

import com.glowcorner.backend.enums.PaymentMethod;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInfo {
    PaymentMethod paymentMethod;
    String paymentIntentId;
    Map<String, Object> extraParams;
}
