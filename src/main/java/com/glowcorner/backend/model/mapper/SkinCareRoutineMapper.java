package com.glowcorner.backend.model.mapper;

import com.glowcorner.backend.entity.mongoDB.SkincareRoutine.SkinCareRoutine;
import com.glowcorner.backend.model.DTO.SkinCareRoutineDTO;
import com.glowcorner.backend.model.mapper.Product.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SkinCareRoutineMapper {

    private final ProductMapper productMapper;

    public SkinCareRoutineMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public SkinCareRoutineDTO toDTO(SkinCareRoutine skinCareRoutine) {
        if (skinCareRoutine == null) {
            return null;
        }
        return new SkinCareRoutineDTO(
            skinCareRoutine.getRoutineID(),
            skinCareRoutine.getSkinType(),
            skinCareRoutine.getRoutineName(),
            skinCareRoutine.getRoutineDescription(),
            skinCareRoutine.getProducts().stream()
                    .map(productMapper::toDTO)
                    .collect(Collectors.toList()));
    }
}
