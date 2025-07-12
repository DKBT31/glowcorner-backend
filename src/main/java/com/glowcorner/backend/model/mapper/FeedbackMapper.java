package com.glowcorner.backend.model.mapper;

import com.glowcorner.backend.entity.mongoDB.Feedback;
import com.glowcorner.backend.model.DTO.FeedbackDTO;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public FeedbackDTO toDTO(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        return new FeedbackDTO(
                feedback.getFeedbackID(),
                feedback.getCustomerID(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getFeedbackDate()
        );
    }

}