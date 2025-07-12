package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.model.DTO.SkinCareRoutineDTO;
import com.glowcorner.backend.model.DTO.request.SkinCareRoutine.CreateRoutineRequest;

import java.util.List;

public interface SkinCareRoutineService {

    SkinCareRoutineDTO createSkinCareRoutine(CreateRoutineRequest request);
    SkinCareRoutineDTO updateSkinCareRoutine(String routineId, SkinCareRoutineDTO skinCareRoutineDTO);
    void deleteSkinCareRoutine(String routineId);
    SkinCareRoutineDTO deleteProductFromRoutine(String routineId, String productId);

    SkinCareRoutineDTO applyRoutineToUser(String userID, String skinCareRoutineID);


    List<SkinCareRoutineDTO> getAllSkinCareRoutines();
    List<SkinCareRoutineDTO> getSkinCareRoutineBySkinType(SkinType skinType);
    List<SkinCareRoutineDTO> getSkinCareRoutineByName(String name);
    SkinCareRoutineDTO getSkinCareRoutineById(String routineId);
}
