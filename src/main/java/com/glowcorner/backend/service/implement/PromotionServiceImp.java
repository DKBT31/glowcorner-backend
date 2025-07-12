package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.Promotion;
import com.glowcorner.backend.model.DTO.PromotionDTO;
import com.glowcorner.backend.model.DTO.request.Promotion.CreatePromotionRequest;
import com.glowcorner.backend.model.mapper.PromotionMapper;
import com.glowcorner.backend.repository.PromotionRepository;
import com.glowcorner.backend.service.interfaces.PromotionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionServiceImp implements PromotionService {

    private final PromotionMapper promotionMapper;
    private final PromotionRepository promotionRepository;
    private final CounterServiceImpl counterServiceImpl;

    public PromotionServiceImp(PromotionMapper promotionMapper, PromotionRepository promotionRepository, CounterServiceImpl counterServiceImpl) {
        this.promotionMapper = promotionMapper;
        this.promotionRepository = promotionRepository;
        this.counterServiceImpl = counterServiceImpl;
    }

    @Override
    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(promotionMapper::toDTO)
                .toList();
    }

    @Override
    public PromotionDTO getPromotionById(String id) {
        Promotion promotion = promotionRepository.findPromotionByPromotionID(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        return promotionMapper.toDTO(promotion);
    }

    @Override
    public List<PromotionDTO> getPromotionByName(String promotionName) {
        List<Promotion> promotions = promotionRepository.findByPromotionNameContainingIgnoreCase(promotionName);
        return promotions.stream()
                .map(promotionMapper::toDTO)
                .toList();
    }

    @Override
    public List<PromotionDTO> getPromotionByProductIDs(List<String> productIDs){
        List<Promotion> promotions = promotionRepository.findByProductIDsIn(productIDs);
        return promotions.stream()
                .map(promotionMapper::toDTO)
                .toList();
    }

    @Override
    public List<PromotionDTO> getActivePromotion(){
        LocalDate now = LocalDate.now();
        List<Promotion> promotion = promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(now, now);
        return promotion.stream()
                .map(promotionMapper::toDTO)
                .toList();
    }

    @Override
    public List<PromotionDTO> getActivePromotionByDate(LocalDate date){
        List<Promotion> promotion = promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
        return promotion.stream()
                .map(promotionMapper::toDTO)
                .toList();
    }

    @Override
    public PromotionDTO getActivePromotionByProductIDs(List<String> productIDs) {
        LocalDate now = LocalDate.now();
        Promotion promotions = promotionRepository.findActivePromotion(now, now, productIDs)
                .orElseThrow(() -> new RuntimeException("No active promotion found"));
        return promotionMapper.toDTO(promotions);
    }

    @Override
    public PromotionDTO createPromotion(CreatePromotionRequest request) {
        Promotion promotion = new Promotion();
        promotion.setPromotionID(counterServiceImpl.getNextPromotionID());
        promotion.setPromotionName(request.getPromotionName());

        if (request.getDiscount() < 5 || request.getDiscount() > 20) {
            throw new RuntimeException("A promotion should be at least 5% and at most 20%");
        }
        promotion.setDiscount(request.getDiscount());

        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setProductIDs(request.getProductIDs());
        boolean exists = promotionRepository.countByProductIDsAndDateRangeOverlap(promotion.getProductIDs(), promotion.getStartDate(), promotion.getEndDate()) > 0;
        if (exists) {
            throw new RuntimeException("A promotion already exists within the given date range for the same product.");
        }

        promotion = promotionRepository.save(promotion);

        return promotionMapper.toDTO(promotion);
    }

    @Override
    public PromotionDTO updatePromotion(String id, PromotionDTO promotionDTO) {
        try {
            Promotion existingPromotion = promotionRepository.findPromotionByPromotionID(id)
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));

            LocalDate startDate = promotionDTO.getStartDate();
            LocalDate endDate = promotionDTO.getEndDate();
            List<String> productIDs = promotionDTO.getProductIDs();

            boolean exists = promotionRepository.countByProductIDsAndDateRangeOverlapExcludeCurrent(productIDs, startDate, endDate, id) > 0;
            if (exists) {
                throw new RuntimeException("A promotion already exists within the given date range for the same product.");
            }

            if (promotionDTO.getPromotionName() != null) existingPromotion.setPromotionName(promotionDTO.getPromotionName());
            if (promotionDTO.getDiscount() != null) existingPromotion.setDiscount(promotionDTO.getDiscount());
            if (promotionDTO.getStartDate() != null) existingPromotion.setStartDate(promotionDTO.getStartDate());
            if (promotionDTO.getEndDate() != null) existingPromotion.setEndDate(promotionDTO.getEndDate());
            if (promotionDTO.getProductIDs() != null) existingPromotion.setProductIDs(promotionDTO.getProductIDs());

            Promotion updatedPromotion = promotionRepository.save(existingPromotion);
            return promotionMapper.toDTO(updatedPromotion);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public void deletePromotion(String id) {
        promotionRepository.deletePromotionByPromotionID(id);
    }
}