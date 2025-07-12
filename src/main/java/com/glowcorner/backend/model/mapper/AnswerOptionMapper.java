package com.glowcorner.backend.model.mapper;

import com.glowcorner.backend.entity.mongoDB.AnswerOption;
import com.glowcorner.backend.model.DTO.AnswerOptionDTO;
import org.springframework.stereotype.Component;

@Component
public class AnswerOptionMapper {

    public AnswerOptionDTO toDTO(AnswerOption answerOption) {
        if (answerOption == null) {
            return null;
        }
        return new AnswerOptionDTO(
                answerOption.getQuestionId(),
                answerOption.getOptionID(),
                answerOption.getSkinType(),
                answerOption.getOptionText()
        );
    }

}