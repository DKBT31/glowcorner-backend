package com.glowcorner.backend.model.DTO.request.Promotion;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePromotionRequest {

    String promotionName;
    List<String> productIDs;
    Integer discount;
    LocalDate startDate;
    LocalDate endDate;

}
