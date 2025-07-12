package com.glowcorner.backend.model.mapper;

import com.glowcorner.backend.entity.mongoDB.Promotion;
import com.glowcorner.backend.model.DTO.PromotionDTO;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionDTO toDTO(Promotion promotion) {
        if (promotion == null) {
            return null;
        }
        return new PromotionDTO(
                promotion.getPromotionID(),
                promotion.getPromotionName(),
                promotion.getProductIDs(),
                promotion.getDiscount(),
                promotion.getStartDate(),
                promotion.getEndDate()
        );
    }

}