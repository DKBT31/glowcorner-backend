package com.glowcorner.backend.controller.PromotionController;

import com.glowcorner.backend.model.DTO.PromotionDTO;
import com.glowcorner.backend.model.DTO.request.Promotion.CreatePromotionRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Promotion Management System", description = "Operations pertaining to promotions in the Promotion Management System")
@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // Get all promotion
    @Operation(summary = "Get all promotions", description = "Retrieve a list of all available promotions")
    @GetMapping
    public ResponseEntity<ResponseData> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.getAllPromotions();
        if (promotions.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404, true, "Promotions found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Promotions found", promotions, null, null));
    }

    // Get a promotion by ID
    @Operation(summary = "Get a promotion by ID", description = "Retrieve a single promotion using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getPromotionById(@PathVariable String id) {
        PromotionDTO promotion = promotionService.getPromotionById(id);
        if (promotion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Promotion with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Promotion found", promotion, null, null));
    }

    // Search promotion by name
    @Operation(summary = "Search promotions by name", description = "Search for promotions using name")
    @GetMapping("/search")
    public ResponseEntity<?> searchPromotionByName(@RequestParam String name) {
        List<PromotionDTO> users = promotionService.getPromotionByName(name);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "There is no user with name '" + name + "'.", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "User found", users, null, null));
    }

    // Search promotion by productID
    @Operation(summary = "Get a promotion by product IDs", description = "Retrieve a promotion by product IDs")
    @GetMapping("/product")
    public ResponseEntity<ResponseData> getPromotionByProductId(@RequestParam List<String> ids) {
        List<PromotionDTO> promotion = promotionService.getPromotionByProductIDs(ids);
        if (promotion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Promotion with product IDs: " + ids + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Promotion found", promotion, null, null));
    }

    // Search active promotion by productID
    @Operation(summary = "Get active promotion by product IDs", description = "Retrieve the currently active promotion by product IDs")
    @GetMapping("/active/product")
    public ResponseEntity<ResponseData> getActivePromotionByProductId(@RequestParam List<String> ids) {
        PromotionDTO promotion = promotionService.getActivePromotionByProductIDs(ids);
        if (promotion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Promotion with productID: " + ids + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Promotion found", promotion, null, null));
    }

    // Get active promotion
    @Operation(summary = "Get active promotion", description = "Retrieve the currently active promotion")
    @GetMapping("/active")
    public ResponseEntity<ResponseData> getActivePromotion() {
        List<PromotionDTO> promotion = promotionService.getActivePromotion();
        return ResponseEntity.ok(new ResponseData(200, true, "Active promotion found", promotion, null, null));
    }

    // Get active promotion
    @Operation(summary = "Get active promotion", description = "Retrieve the currently active promotion")
    @GetMapping("/active/{date}")
    public ResponseEntity<ResponseData> getActivePromotionByDate(@PathVariable LocalDate date) {
        List<PromotionDTO> promotion = promotionService.getActivePromotionByDate(date);
        return ResponseEntity.ok(new ResponseData(200, true, "Active promotion found", promotion, null, null));
    }

    // Create a new promotion
    @Operation(summary = "Create a new promotion", description = "Add a new promotion to the catalog")
    @PostMapping
    public ResponseEntity<ResponseData> createPromotion(@RequestBody CreatePromotionRequest request) {
        try {
            PromotionDTO createdPromotion = promotionService.createPromotion(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseData(201, true, "Promotion created", createdPromotion, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Fail to create promotion: " + e.getMessage(), null, null, null));
        }
    }

    // Update an existing promotion
    @Operation(summary = "Update a promotion", description = "Update an existing promotion using its ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updatePromotion(@PathVariable String id, @RequestBody PromotionDTO promotionDTO) {
        try {
            PromotionDTO updatedPromotion = promotionService.updatePromotion(id, promotionDTO);
            if (updatedPromotion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseData(404, false, "Promotion with ID: " + id + " not found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "Promotion updated", updatedPromotion, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Fail to update promotion with ID: " + id + ". Error: " + e.getMessage(), null, null, null));
        }
    }

    // Delete a promotion
    @Operation(summary = "Delete a promotion by ID", description = "Remove a promotion from the system using its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable String id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}