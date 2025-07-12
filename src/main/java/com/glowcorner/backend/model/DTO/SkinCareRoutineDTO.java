package com.glowcorner.backend.model.DTO;

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
public class SkinCareRoutineDTO {

    String routineID;
    SkinType skinType;
    String routineName;
    String routineDescription;
    List<ProductDTO> productDTOS;

}
