package com.glowcorner.backend.model.mapper.CreateMapper.SkinCareRoutine;

import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.entity.mongoDB.SkincareRoutine.SkinCareRoutine;
import com.glowcorner.backend.enums.Status.RoutineStatus;
import com.glowcorner.backend.model.DTO.request.SkinCareRoutine.CreateRoutineRequest;
import com.glowcorner.backend.repository.ProductRepository;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreateRoutineRequestMapper {

    private final CounterServiceImpl counterServiceImpl;
    private final ProductRepository productRepository;

    public CreateRoutineRequestMapper(CounterServiceImpl counterServiceImpl, ProductRepository promotionRepository) {
        this.counterServiceImpl = counterServiceImpl;
        this.productRepository = promotionRepository;
    }

    public SkinCareRoutine fromCreateRequest(CreateRoutineRequest request) {
        if (request == null){
            return null;
        }

        SkinCareRoutine skinCareRoutine = new SkinCareRoutine();
        skinCareRoutine.setRoutineID(counterServiceImpl.getNextSkinCareRoutineID());
        skinCareRoutine.setSkinType(request.getSkinType());
        skinCareRoutine.setRoutineName(request.getRoutineName());
        skinCareRoutine.setRoutineDescription(request.getRoutineDescription());
        List<Product> products = request.getProductIDs().stream()
                .map(productRepository::findByProductID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        skinCareRoutine.setProducts(products);
        skinCareRoutine.setStatus(RoutineStatus.ACTIVE);

        return skinCareRoutine;
    }

}
