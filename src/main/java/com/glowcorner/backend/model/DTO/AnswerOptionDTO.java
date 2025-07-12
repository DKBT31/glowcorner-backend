package com.glowcorner.backend.model.DTO;

import com.glowcorner.backend.enums.SkinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerOptionDTO {

    String questionID;
    String optionID;
    SkinType skinType;
    String optionText;
}