package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.model.DTO.PromotionDTO;
import com.glowcorner.backend.model.DTO.request.Promotion.CreatePromotionRequest;

import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    List<PromotionDTO> getAllPromotions();
    List<PromotionDTO> getPromotionByName(String promotionName);
    PromotionDTO getPromotionById(String id);
    List<PromotionDTO> getPromotionByProductIDs(List<String> productIDs);
    List<PromotionDTO> getActivePromotion();
    List<PromotionDTO> getActivePromotionByDate(LocalDate date);
    PromotionDTO getActivePromotionByProductIDs(List<String> productIDs);

    PromotionDTO createPromotion(CreatePromotionRequest request);
    PromotionDTO updatePromotion(String id, PromotionDTO promotionDTO);
    void deletePromotion(String id);
}