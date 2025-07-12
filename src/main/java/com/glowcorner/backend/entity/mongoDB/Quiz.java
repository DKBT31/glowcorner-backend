package com.glowcorner.backend.entity.mongoDB;

import com.glowcorner.backend.enums.Status.QuizStatus;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "quiz")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Quiz {

    @Id
    String id;

    String questionId;

    String quizText;

    List<AnswerOption> options;

    QuizStatus status;

}
