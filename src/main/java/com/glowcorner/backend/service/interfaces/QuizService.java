package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.model.DTO.QuizDTO;
import com.glowcorner.backend.model.DTO.request.Quiz.CreateQuizRequest;

import java.util.List;

public interface QuizService {

    List<QuizDTO> getAllQuizzes();

    QuizDTO getQuizById(String id);

    QuizDTO createQuiz(CreateQuizRequest request);

    QuizDTO updateQuiz(String id, QuizDTO quizDTO);

    void deleteQuiz(String id);
}