package com.glowcorner.backend.controller.QuizController;

import com.glowcorner.backend.model.DTO.QuizDTO;
import com.glowcorner.backend.model.DTO.request.Quiz.CreateQuizRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Quiz Management System", description = "Operations pertaining to quizzes in the Quiz Management System")
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @Operation(summary = "Get all quizzes", description = "Retrieve a list of all available quizzes")
    @GetMapping
    public ResponseEntity<ResponseData> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(new ResponseData(200, true, "Quizzes found", quizzes, null, null));
    }

    @Operation(summary = "Get a quiz by ID", description = "Retrieve a single quiz using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getQuizById(@PathVariable String id) {
        QuizDTO quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Quiz with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Quiz found", quiz, null, null));
    }

    @Operation(summary = "Create a new quiz", description = "Add a new quiz to the catalog")
    @PostMapping
    public ResponseEntity<ResponseData> createQuiz(@RequestBody CreateQuizRequest request) {
        QuizDTO createdQuiz = quizService.createQuiz(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(201, true, "Quiz created", createdQuiz, null, null));
    }

    @Operation(summary = "Update a quiz", description = "Update an existing quiz using its ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> updateQuiz(@PathVariable String id, @RequestBody QuizDTO quizDTO) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, quizDTO);
        if (updatedQuiz == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Quiz with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Quiz updated", updatedQuiz, null, null));
    }

    @Operation(summary = "Delete a quiz by ID", description = "Remove a quiz from the system using its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }
}