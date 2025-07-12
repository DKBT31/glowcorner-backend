package com.glowcorner.backend.model.DTO.request.Feedback;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFeedbackRequest {

    String customerID;
    int rating;
    String comment;
    LocalDate feedbackDate;

}
