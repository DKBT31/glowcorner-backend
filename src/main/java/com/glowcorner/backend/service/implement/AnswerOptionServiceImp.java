package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.AnswerOption;
import com.glowcorner.backend.enums.Status.OptionStatus;
import com.glowcorner.backend.model.DTO.AnswerOptionDTO;
import com.glowcorner.backend.model.mapper.AnswerOptionMapper;
import com.glowcorner.backend.repository.AnswerOptionRepository;
import com.glowcorner.backend.service.interfaces.AnswerOptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerOptionServiceImp implements AnswerOptionService {
    private final AnswerOptionRepository answerOptionRepository;
    private final AnswerOptionMapper answerOptionMapper;

    public AnswerOptionServiceImp(AnswerOptionRepository answerOptionRepository, AnswerOptionMapper answerOptionMapper) {
        this.answerOptionRepository = answerOptionRepository;
        this.answerOptionMapper = answerOptionMapper;
    }

    @Override
    public List<AnswerOptionDTO> getAnswerOptionByQuestionID(String questionID) {
        List<AnswerOption> answerOptions = answerOptionRepository.findAnswerOptionsByQuestionId(questionID);
        return answerOptions.stream()
                .map(answerOption -> {
                    AnswerOptionDTO answerOptionDTO = answerOptionMapper.toDTO(answerOption);
                    return answerOptionDTO;
                })
                .toList();
    }

    @Override
    public void disableAnswerOption(String answerOptionId) {
        AnswerOption answerOption = answerOptionRepository.findById(answerOptionId).orElseThrow(() -> new RuntimeException("AnswerOption not found"));
        answerOption.setStatus(OptionStatus.DISABLE); // Set status to false (disabled)
        answerOptionRepository.save(answerOption);
    }
}
