package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.entity.mongoDB.SkincareRoutine.SkinCareRoutine;
import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Status.RoutineStatus;
import com.glowcorner.backend.model.DTO.ProductDTO;
import com.glowcorner.backend.model.DTO.SkinCareRoutineDTO;
import com.glowcorner.backend.model.DTO.request.SkinCareRoutine.CreateRoutineRequest;
import com.glowcorner.backend.model.mapper.CreateMapper.SkinCareRoutine.CreateRoutineRequestMapper;
import com.glowcorner.backend.model.mapper.SkinCareRoutineMapper;
import com.glowcorner.backend.repository.ProductRepository;
import com.glowcorner.backend.repository.SkinCareRoutineRepository;
import com.glowcorner.backend.repository.UserRepository;
import com.glowcorner.backend.service.interfaces.SkinCareRoutineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkinCareRoutineServiceImp implements SkinCareRoutineService {

    private final SkinCareRoutineMapper skinCareRoutineMapper;

    private final SkinCareRoutineRepository skinCareRoutineRepository;

    private final CreateRoutineRequestMapper createRoutineRequestMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SkinCareRoutineServiceImp(SkinCareRoutineMapper skinCareRoutineMapper, SkinCareRoutineRepository skinCareRoutineRepository, CreateRoutineRequestMapper createRoutineRequestMapper, ProductRepository productRepository, UserRepository userRepository) {
        this.skinCareRoutineMapper = skinCareRoutineMapper;
        this.skinCareRoutineRepository = skinCareRoutineRepository;
        this.createRoutineRequestMapper = createRoutineRequestMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Get all Skincare routine
    @Override
    public List<SkinCareRoutineDTO> getAllSkinCareRoutines() {
        List<SkinCareRoutine> skinCareRoutines = skinCareRoutineRepository.findAll();
        return skinCareRoutines.stream()
                .map(skinCareRoutineMapper::toDTO)
                .toList();
    }

    // Get skincare routine by category
    @Override
    public List<SkinCareRoutineDTO> getSkinCareRoutineBySkinType(SkinType skinType) {
        List<SkinCareRoutine> skinCareRoutines = skinCareRoutineRepository.findBySkinType(skinType);
        return skinCareRoutines.stream()
                .map(skinCareRoutineMapper::toDTO)
                .toList();
    }

    // Search routine
    @Override
    public List<SkinCareRoutineDTO> getSkinCareRoutineByName(String name){
        List<SkinCareRoutine> skinCareRoutines = skinCareRoutineRepository.findByRoutineNameContainingIgnoreCase(name);
        return skinCareRoutines.stream()
                .map(skinCareRoutineMapper::toDTO)
                .toList();
    }

    // Get routine by id
    @Override
    public SkinCareRoutineDTO getSkinCareRoutineById(String routineId) {
        SkinCareRoutine skinCareRoutine = skinCareRoutineRepository.findByRoutineID(routineId)
                .orElseThrow(() -> new RuntimeException("Skin care routine not found"));
        return skinCareRoutineMapper.toDTO(skinCareRoutine);
    }

    // Create a routine
    @Override
    public SkinCareRoutineDTO createSkinCareRoutine(CreateRoutineRequest request) {
        SkinCareRoutine skinCareRoutine = createRoutineRequestMapper.fromCreateRequest(request);
        skinCareRoutine = skinCareRoutineRepository.save(skinCareRoutine);
        return skinCareRoutineMapper.toDTO(skinCareRoutine);
    }

    // Update a routine
    @Override
    public SkinCareRoutineDTO updateSkinCareRoutine(String routineId, SkinCareRoutineDTO skinCareRoutineDTO) {
        try {
            SkinCareRoutine existingRoutine = skinCareRoutineRepository.findByRoutineID(routineId)
                    .orElseThrow(() -> new RuntimeException("Skin care routine not found"));

            if (skinCareRoutineDTO.getSkinType() != null) existingRoutine.setSkinType(skinCareRoutineDTO.getSkinType());
            if (skinCareRoutineDTO.getRoutineName() != null) existingRoutine.setRoutineName(skinCareRoutineDTO.getRoutineName());
            if (skinCareRoutineDTO.getRoutineDescription() != null) existingRoutine.setRoutineDescription(skinCareRoutineDTO.getRoutineDescription());

            if (skinCareRoutineDTO.getProductDTOS() != null) {
                List<Product> existingProducts = existingRoutine.getProducts();
                List<Product> updatedProducts = skinCareRoutineDTO.getProductDTOS().stream()
                        .map(ProductDTO::getProductID)
                        .map(productRepository::findByProductID)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                // Update existing products
                for (Product updatedProduct : updatedProducts) {
                    boolean productExists = existingProducts.stream()
                            .anyMatch(existingProduct -> existingProduct.getProductID().equals(updatedProduct.getProductID()));
                    if (!productExists) {
                        existingProducts.add(updatedProduct);
                    }
                }

                existingRoutine.setProducts(existingProducts);
            }

            SkinCareRoutine updatedRoutine = skinCareRoutineRepository.save(existingRoutine);
            return skinCareRoutineMapper.toDTO(updatedRoutine);
        } catch (Exception e) {
            throw new RuntimeException("Fail to update SkincareRoutine: " + e.getMessage(), e);
        }
    }

    @Override
    public SkinCareRoutineDTO applyRoutineToUser(String userID, String skinCareRoutineID) {
        try {
            User user = userRepository.findByUserID(userID)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            SkinCareRoutine skinCareRoutine = skinCareRoutineRepository.findByRoutineID(skinCareRoutineID)
                    .orElseThrow(() -> new RuntimeException("Skin care routine not found"));

            user.setSkinCareRoutine(skinCareRoutine);
            userRepository.save(user);

            return skinCareRoutineMapper.toDTO(skinCareRoutine);
        } catch (Exception e) {
            throw new RuntimeException("Fail to apply skincare routine to user: " + e.getMessage(), e);
        }
    }

    // Delete a routine
    @Override
    public void deleteSkinCareRoutine(String routineId) {
        SkinCareRoutine existingRoutine = skinCareRoutineRepository.findByRoutineID(routineId)
                .orElseThrow(() -> new RuntimeException("Skin care routine not found"));
        existingRoutine.setStatus(RoutineStatus.DISABLE); // Assuming RoutineStatus enum has DISABLE status
        skinCareRoutineRepository.save(existingRoutine);
    }

    // Delete a product from a skincare routine
    @Override
    public SkinCareRoutineDTO deleteProductFromRoutine(String routineId, String productId) {
        try {
            SkinCareRoutine existingRoutine = skinCareRoutineRepository.findByRoutineID(routineId)
                    .orElseThrow(() -> new RuntimeException("Skin care routine not found"));

            List<Product> updatedProducts = existingRoutine.getProducts().stream()
                    .filter(product -> !product.getProductID().equals(productId))
                    .collect(Collectors.toList());

            existingRoutine.setProducts(updatedProducts);
            SkinCareRoutine updatedRoutine = skinCareRoutineRepository.save(existingRoutine);
            return skinCareRoutineMapper.toDTO(updatedRoutine);
        } catch (Exception e) {
            throw new RuntimeException("Fail to delete product from SkincareRoutine: " + e.getMessage(), e);
        }
    }

}
