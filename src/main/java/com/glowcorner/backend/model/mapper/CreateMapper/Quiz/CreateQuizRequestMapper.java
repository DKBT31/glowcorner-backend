package com.glowcorner.backend.model.mapper.CreateMapper.Quiz;

import com.glowcorner.backend.entity.mongoDB.Quiz;
import com.glowcorner.backend.model.DTO.request.Quiz.CreateQuizRequest;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CreateQuizRequestMapper {

    private final CounterServiceImpl counterService;
    private final CreateAnswerOptionRequestMapper createAnswerOptionRequestMapper;

    public CreateQuizRequestMapper(CounterServiceImpl counterService, CreateAnswerOptionRequestMapper createAnswerOptionRequestMapper) {
        this.counterService = counterService;
        this.createAnswerOptionRequestMapper = createAnswerOptionRequestMapper;
    }

    public Quiz fromCreateRequest(CreateQuizRequest request) {
        if (request == null) {
            return null;
        }
        Quiz quiz = new Quiz();
        quiz.setQuestionId(counterService.getNextQuestionID());
        quiz.setQuizText(request.getQuizText());
        quiz.setOptions(request.getAnswerOptionRequests().stream()
                .map(options -> createAnswerOptionRequestMapper.fromCreateRequest(options, quiz.getQuestionId()))
                .collect(Collectors.toList()));
        return quiz;
    }
}
