package com.glowcorner.backend.model.mapper.CreateMapper.Quiz;

import com.glowcorner.backend.entity.mongoDB.AnswerOption;
import com.glowcorner.backend.model.DTO.request.Quiz.CreateAnswerOptionRequest;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateAnswerOptionRequestMapper {

    private final CounterServiceImpl counterService;

    public CreateAnswerOptionRequestMapper(CounterServiceImpl counterService) {
        this.counterService = counterService;
    }

    public AnswerOption fromCreateRequest(CreateAnswerOptionRequest request, String quizID) {
        if (request == null) {
            return null;
        }
        AnswerOption answerOption = new AnswerOption();
        answerOption.setOptionID(counterService.getNextOptionID());
        answerOption.setQuestionId(quizID);
        answerOption.setSkinType(request.getSkinType());
        answerOption.setOptionText(request.getOptionText());
        return answerOption;
    }
}
