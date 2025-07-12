package com.glowcorner.backend.controller.UserController;

import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.model.DTO.User.UserDTOByCustomer;
import com.glowcorner.backend.model.DTO.request.User.CreateCustomerRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.CloudinaryService;
import com.glowcorner.backend.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Management System (Customer)", description = "Operations pertaining to users in the User Management System")
@RestController
@RequestMapping("/api/user")
public class UserControllerCustomer {

    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public UserControllerCustomer(UserService userService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    // Create user
    @Operation(summary = "Create a new customer", description = "Add a new customer to the system")
    @PostMapping
    public ResponseEntity<ResponseData> createUser(@RequestBody CreateCustomerRequest request) {
        UserDTOByCustomer createdUser = userService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(201, true, "User created", createdUser, null, null));
    }

    // Update user
    @Operation(summary = "Update a user by ID", description = "Update a user using its ID")
    @PutMapping(value = "{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData> updateUserByCustomer(
            @PathVariable String userId,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "skinType", required = false) String skinType,
            @RequestPart(value = "image", required = false) @Parameter(description = "User's avatar image file", content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)) MultipartFile imageFile) {
        try {
            System.out.println("=== RECEIVED PARAMETERS ===");
            System.out.println("fullName: " + fullName);
            System.out.println("email: " + email);
            System.out.println("phone: " + phone);
            System.out.println("address: " + address);
            System.out.println("skinType: " + skinType);
            System.out.println("=== END PARAMETERS ===");

            // Get the existing user data
            UserDTOByCustomer userDTO = userService.getUserByIdForCustomer(userId);
            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseData(404, false, "User with ID: " + userId + " not found", null, null, null));
            }

            // Update only the fields that were provided
            if (fullName != null && !fullName.trim().isEmpty()) {
                // Remove quotes if present and trim whitespace
                String cleanFullName = fullName.trim().replaceAll("^\"|\"$", "");
                userDTO.setFullName(cleanFullName);
            }
            if (email != null && !email.trim().isEmpty()) {
                // Remove quotes if present and trim whitespace
                String cleanEmail = email.trim().replaceAll("^\"|\"$", "");
                userDTO.setEmail(cleanEmail);
            }
            if (phone != null && !phone.trim().isEmpty()) {
                // Remove quotes if present and trim whitespace
                String cleanPhone = phone.trim().replaceAll("^\"|\"$", "");
                userDTO.setPhone(cleanPhone);
            }
            if (address != null && !address.trim().isEmpty()) {
                // Remove quotes if present and trim whitespace
                String cleanAddress = address.trim().replaceAll("^\"|\"$", "");
                userDTO.setAddress(cleanAddress);
            }
            if (skinType != null && !skinType.trim().isEmpty()) {
                try {
                    // Remove quotes if present and trim whitespace
                    String cleanSkinType = skinType.trim().replaceAll("^\"|\"$", "");
                    System.out.println("Original skin type: " + skinType);
                    System.out.println("Cleaned skin type: " + cleanSkinType);

                    SkinType skinTypeEnum = SkinType.valueOf(cleanSkinType);
                    userDTO.setSkinType(skinTypeEnum);
                    System.out.println("Successfully set skin type to: " + skinTypeEnum);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid skin type value: " + skinType);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseData(400, false, "Invalid skin type: " + skinType, null, null, null));
                }
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(imageFile);
                userDTO.setAvatar_url(imageUrl);
            }

            UserDTOByCustomer updatedUser = userService.updateUserByCustomer(userId, userDTO);
            return ResponseEntity.ok(new ResponseData(200, true, "User updated", updatedUser, null, null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Failed to update user: " + e.getMessage(), null, null, null));
        }
    }

    // Get customer by id
    @Operation(summary = "Get a customer by ID", description = "Retrieve a single customer using its ID")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        UserDTOByCustomer user = userService.getUserByIdForCustomer(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "User with ID: " + userId + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "User found", user, null, null));
    }
}