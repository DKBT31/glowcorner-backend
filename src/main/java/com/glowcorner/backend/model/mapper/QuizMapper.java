package com.glowcorner.backend.model.mapper;

import com.glowcorner.backend.entity.mongoDB.Quiz;
import com.glowcorner.backend.model.DTO.QuizDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class QuizMapper {

    private final AnswerOptionMapper answerOptionMapper;

    public QuizMapper(AnswerOptionMapper answerOptionMapper) {
        this.answerOptionMapper = answerOptionMapper;
    }

    public QuizDTO toDTO(Quiz quiz) {
        if (quiz == null) {
            return null;
        }
        return new QuizDTO(
                quiz.getQuestionId(),
                quiz.getQuizText(),
                quiz.getOptions().stream()
                        .map(answerOptionMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

}