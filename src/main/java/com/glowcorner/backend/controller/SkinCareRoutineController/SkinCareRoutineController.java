package com.glowcorner.backend.controller.SkinCareRoutineController;

import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.model.DTO.SkinCareRoutineDTO;
import com.glowcorner.backend.model.DTO.request.SkinCareRoutine.CreateRoutineRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.SkinCareRoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Skin Care Routine Management System", description = "Operations pertaining to skin care routines in the Skin Care Routine Management System")
@RestController
@RequestMapping("/api/skin-care-routines")
public class SkinCareRoutineController {

    private final SkinCareRoutineService skinCareRoutineService;

    public SkinCareRoutineController(SkinCareRoutineService skinCareRoutineService) {
        this.skinCareRoutineService = skinCareRoutineService;
    }

    // Get all skincare routine
    @Operation(summary = "Get all skin care routines", description = "Retrieve a list of all available skin care routines")
    @GetMapping
    public ResponseEntity<List<SkinCareRoutineDTO>> getAllSkinCareRoutines() {
        List<SkinCareRoutineDTO> routines = skinCareRoutineService.getAllSkinCareRoutines();
        return ResponseEntity.ok(routines);
    }

    // Get skincare routine by ID
    @Operation(summary = "Get a skincare routine by ID", description = "Retrieve a single skin care routine using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getSkinCareRoutineById(@PathVariable String id) {
        SkinCareRoutineDTO routine = skinCareRoutineService.getSkinCareRoutineById(id);
        if (routine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Skin care routine with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Skin care routine found", routine, null, null));
    }

    // Get skincare routine by category
    @Operation(summary = "Get skincare routines by category", description = "Retrieve a list of skin care routines using their category")
    @GetMapping("/skinType/{skinType}")
    public ResponseEntity<ResponseData> getSkinCareRoutineBySkinType(@PathVariable SkinType skinType) {
        List<SkinCareRoutineDTO> routines = skinCareRoutineService.getSkinCareRoutineBySkinType(skinType);
        if (routines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "There is no skin care routine in category: " + skinType, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Skin care routines found", routines, null, null));
    }

    // Get skincare routine by name
    @Operation(summary = "Get skincare routines by name", description = "Retrieve a list of skin care routines using their name")
    @GetMapping("/search")
    public ResponseEntity<ResponseData> getSkinCareRoutineByName(@RequestParam String name) {
        List<SkinCareRoutineDTO> routines = skinCareRoutineService.getSkinCareRoutineByName(name);
        if (routines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "There is no skin care routine with name: " + name, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Skin care routines found", routines, null, null));
    }

    // Create new skincare routine
    @Operation(summary = "Create a new skincare routine", description = "Add a new skin care routine to the catalog")
    @PostMapping
    public ResponseEntity<ResponseData> createSkinCareRoutine(@RequestBody CreateRoutineRequest request) {
        SkinCareRoutineDTO createdRoutine = skinCareRoutineService.createSkinCareRoutine(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(201, true, "Skin care routine created", createdRoutine, null, null));
    }

    // Update an existing skincare routine
    @Operation(summary = "Update a skincare routine", description = "Update an existing skin care routine using its ID " +
            "*(If update product, only type 'productID', and it only add a new product into routine)*")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateSkinCareRoutine(@PathVariable String id, @RequestBody SkinCareRoutineDTO skinCareRoutineDTO) {
        try {
            SkinCareRoutineDTO updatedRoutine = skinCareRoutineService.updateSkinCareRoutine(id, skinCareRoutineDTO);
            if (updatedRoutine == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseData(404, false, "Skin care routine with ID: " + id + " not found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "Skin care routine updated", updatedRoutine, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Fail to update skin care routine with ID: " + id, null, null, null));
        }
    }

    @Operation(summary = "Apply a skincare routine to a user", description = "Assign a skincare routine to a user using their IDs")
    @PostMapping("/{skinCareRoutineID}/apply-to-user/{userID}")
    public ResponseEntity<ResponseData> applyRoutineToUser(@PathVariable String userID, @PathVariable String skinCareRoutineID) {
        try {
            SkinCareRoutineDTO appliedRoutine = skinCareRoutineService.applyRoutineToUser(userID, skinCareRoutineID);
            return ResponseEntity.ok(new ResponseData(200, true, "Skincare routine applied to user", appliedRoutine, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Fail to apply skincare routine to user", null, null, null));
        }
    }

    // Delete a skincare routine
    @Operation(summary = "Delete a skincare routine by ID", description = "Remove a skin care routine from the system using its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkinCareRoutine(@PathVariable String id) {
        skinCareRoutineService.deleteSkinCareRoutine(id);
        return ResponseEntity.noContent().build();
    }

    // Delete a product from a skincare routine
    @Operation(summary = "Delete a product from a skincare routine", description = "Remove a product from a skin care routine using its ID")
    @DeleteMapping("/{routineId}/products/{productId}")
    public ResponseEntity<ResponseData> deleteProductFromRoutine(@PathVariable String routineId, @PathVariable String productId) {
        try {
            SkinCareRoutineDTO updatedRoutine = skinCareRoutineService.deleteProductFromRoutine(routineId, productId);
            return ResponseEntity.ok(new ResponseData(200, true, "Product deleted from skin care routine", updatedRoutine, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Fail to delete product from skin care routine with ID: " + routineId, null, null, null));
        }
    }
}