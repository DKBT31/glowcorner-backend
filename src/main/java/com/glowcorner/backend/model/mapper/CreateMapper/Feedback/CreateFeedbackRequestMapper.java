package com.glowcorner.backend.model.mapper.CreateMapper.Feedback;

import com.glowcorner.backend.entity.mongoDB.Feedback;
import com.glowcorner.backend.model.DTO.request.Feedback.CreateFeedbackRequest;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateFeedbackRequestMapper {

    private final CounterServiceImpl counterService;

    public CreateFeedbackRequestMapper(CounterServiceImpl counterService) {
        this.counterService = counterService;
    }

    public Feedback fromCreateRequest(CreateFeedbackRequest request) {
        if (request == null) {
            return null;
        }
        Feedback feedback = new Feedback();
        feedback.setFeedbackID(counterService.getNextFeedbackID());
        feedback.setCustomerID(request.getCustomerID());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setFeedbackDate(request.getFeedbackDate());
        return feedback;
    }
}
