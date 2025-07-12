package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends MongoRepository<Promotion, String> {

    void deletePromotionByPromotionID(String promotionID);

    Optional<Promotion> findPromotionByPromotionID(String promotionID);
    List<Promotion> findByPromotionNameContainingIgnoreCase(String promotionName);
    List<Promotion> findByProductIDsIn(Collection<String> productIDs);
    List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

    @Query("{ 'startDate': { '$lte': ?0 }, 'endDate': { '$gte': ?1 }, 'productIDs': { '$in': ?2 } }")
    Optional<Promotion> findActivePromotion(LocalDate startDate, LocalDate endDate, List<String> productIDs);

    @Query(value = "{ 'productIDs': { '$in': ?0 }, '$or': [ { 'startDate': { '$lte': ?2 }, 'endDate': { '$gte': ?1 } } ] }", count = true)
    long countByProductIDsAndDateRangeOverlap(List<String> productIDs, LocalDate startDate, LocalDate endDate);

    @Query(value = "{ 'productIDs': { '$in': ?0 }, 'promotionID': { '$ne': ?3 }, '$or': [ { 'startDate': { '$lte': ?2 }, 'endDate': { '$gte': ?1 } } ] }", count = true)
    long countByProductIDsAndDateRangeOverlapExcludeCurrent(List<String> productIDs, LocalDate startDate, LocalDate endDate, String excludePromotionID);
}