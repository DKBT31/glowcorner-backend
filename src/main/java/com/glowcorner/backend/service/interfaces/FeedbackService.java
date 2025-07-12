package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.model.DTO.FeedbackDTO;
import com.glowcorner.backend.model.DTO.request.Feedback.CreateFeedbackRequest;

import java.util.List;

public interface FeedbackService {

    List<FeedbackDTO> getAllFeedbacks();

    FeedbackDTO getFeedbackById(String id);

    FeedbackDTO createFeedback(CreateFeedbackRequest request);

    FeedbackDTO updateFeedback(String id, FeedbackDTO feedbackDTO);

    void deleteFeedback(String id);
}