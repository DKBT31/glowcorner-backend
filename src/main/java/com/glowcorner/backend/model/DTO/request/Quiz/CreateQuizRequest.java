package com.glowcorner.backend.model.DTO.request.Quiz;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateQuizRequest {

    String quizText;

    List<CreateAnswerOptionRequest> answerOptionRequests;

}
