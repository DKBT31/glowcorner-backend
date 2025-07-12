package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.AnswerOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerOptionRepository extends MongoRepository<AnswerOption, String> {
    void deleteAllByQuestionId(String questionID);

    Optional<AnswerOption> findAnswerOptionByOptionID(String optionID);
    List<AnswerOption> findAnswerOptionsByQuestionId(String questionID);

}