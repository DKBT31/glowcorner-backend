package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.entity.mongoDB.AnswerOption;
import com.glowcorner.backend.model.DTO.AnswerOptionDTO;

import java.util.List;

public interface AnswerOptionService {
    void disableAnswerOption(String answerOptionId);

    List<AnswerOptionDTO> getAnswerOptionByQuestionID(String questionID);
}
