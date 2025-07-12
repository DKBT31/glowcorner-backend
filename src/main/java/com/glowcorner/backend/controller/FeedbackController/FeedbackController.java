package com.glowcorner.backend.controller.FeedbackController;

import com.glowcorner.backend.model.DTO.FeedbackDTO;
import com.glowcorner.backend.model.DTO.request.Feedback.CreateFeedbackRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Feedback Management System", description = "Operations pertaining to feedbacks in the Feedback Management System")
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Get all feedbacks", description = "Retrieve a list of all available feedbacks")
    @GetMapping
    public ResponseEntity<ResponseData> getAllFeedbacks() {
        List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(new ResponseData(200, true, "Feedbacks found", feedbacks, null, null));
    }

    @Operation(summary = "Get a feedback by ID", description = "Retrieve a single feedback using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getFeedbackById(@PathVariable String id) {
        FeedbackDTO feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Feedback with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Feedback found", feedback, null, null));
    }

    @Operation(summary = "Create a new feedback", description = "Add a new feedback to the catalog")
    @PostMapping
    public ResponseEntity<ResponseData> createFeedback(@RequestBody CreateFeedbackRequest request) {
        FeedbackDTO createdFeedback = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(201, true, "Feedback created", createdFeedback, null, null));
    }

    @Operation(summary = "Update a feedback", description = "Update an existing feedback using its ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateFeedback(@PathVariable String id, @RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackDTO);
        if (updatedFeedback == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Feedback with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Feedback updated", updatedFeedback, null, null));
    }

    @Operation(summary = "Delete a feedback by ID", description = "Remove a feedback from the system using its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}