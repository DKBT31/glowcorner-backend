package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.SkincareRoutine.SkinCareRoutine;
import com.glowcorner.backend.enums.SkinType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkinCareRoutineRepository extends MongoRepository<SkinCareRoutine, String> {
    Optional<SkinCareRoutine> findByRoutineID(String id);
    List<SkinCareRoutine> findBySkinType(SkinType skinType);
    List<SkinCareRoutine> findByRoutineNameContainingIgnoreCase(String routineName);
    void deleteSkinCareRoutineByRoutineID(String routineID);
}
