package com.glowcorner.backend.controller.QuizController;

import com.glowcorner.backend.model.DTO.AnswerOptionDTO;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.AnswerOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Answer Option Management System", description = "Operations pertaining to answer options in the Quiz Management System")
@RestController
@RequestMapping("/api/answer-options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    public AnswerOptionController(AnswerOptionService answerOptionService) {
        this.answerOptionService = answerOptionService;
    }

    @Operation(summary = "Get answer options by question ID", description = "Retrieve a list of answer options for a specific question")
    @GetMapping("/question/{questionID}")
    public ResponseEntity<ResponseData> getAnswerOptionsByQuestionID(@PathVariable String questionID) {
        try {
            List<AnswerOptionDTO> answerOptions = answerOptionService.getAnswerOptionByQuestionID(questionID);
            return ResponseEntity.ok(new ResponseData(200, true, "Answer options retrieved successfully", answerOptions, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Failed to retrieve answer options: " + e.getMessage(), null, null, null));
        }
    }

    @Operation(summary = "Disable an answer option", description = "Disable an answer option by changing its status to DISABLE")
    @PutMapping("/disable/{id}")
    public ResponseEntity<ResponseData> disableAnswerOption(@PathVariable String id) {
        try {
            answerOptionService.disableAnswerOption(id);
            return ResponseEntity.ok(new ResponseData(200, true, "Answer option disabled successfully", null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Failed to disable answer option: " + e.getMessage(), null, null, null));
        }
    }
}