package com.glowcorner.backend.model.DTO.request.SkinCareRoutine;

import com.glowcorner.backend.enums.SkinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateRoutineRequest {

    SkinType skinType;
    String routineName;
    String routineDescription;
    List<String> productIDs;

}
