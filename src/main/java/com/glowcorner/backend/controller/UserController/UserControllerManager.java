package com.glowcorner.backend.controller.UserController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glowcorner.backend.model.DTO.User.UserDTOByManager;
import com.glowcorner.backend.model.DTO.request.Product.CreateProductRequest;
import com.glowcorner.backend.model.DTO.request.User.CreateUserRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.CloudinaryService;
import com.glowcorner.backend.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "User Management System (Manager)", description = "Operations pertaining to users in the User Management System")
@RestController
@RequestMapping("/api/manager/users")
public class UserControllerManager {

    private final UserService userService;

    private final CloudinaryService cloudinaryService;

    public UserControllerManager(UserService userService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    // Get all users
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public ResponseEntity<List<UserDTOByManager>> getAllUsers() {
        List<UserDTOByManager> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by id
    @Operation(summary = "Get a user by ID", description = "Retrieve a single user using its ID")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        UserDTOByManager user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "User with ID: " + userId + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "User found", user, null, null));
    }

    // Search user by name
    @Operation(summary = "Search users by name", description = "Search for users using name")
    @GetMapping("/search")
    public ResponseEntity<?> searchUserByName(@RequestParam String name) {
        List<UserDTOByManager> users = userService.searchUserByNameManager(name);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "There is no user with name '" + name + "'.", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "User found", users, null, null));
    }

    // Get user by email
    @Operation(summary = "Get a user by email", description = "Retrieve a single user using its email")
    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseData> getUserByEmail(@PathVariable String email) {
        UserDTOByManager user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "User with email: " + email + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "User found", user, null, null));
    }

    // Create a new user
    @Operation(summary = "Create a new user", description = "Add a new user to the system")
    @PostMapping
    public ResponseEntity<ResponseData> createUser(@RequestBody CreateUserRequest request) {
        UserDTOByManager createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(201, true, "User created", createdUser, null, null));
    }

    // Update user
    @Operation(summary = "Update a user", description = "Update an existing user using their ID")
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData> updateUserByManager(
            @PathVariable String userId,
            @RequestPart(value = "user", required = false) @Parameter(
                    description = "User data in JSON format",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ) String userJson,
            @RequestPart(value = "image", required = false) @Parameter(
                    description = "User's avatar image file",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)
            ) MultipartFile imageFile) {
        try {
            UserDTOByManager userDTO;
            if (userJson != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                userDTO = objectMapper.readValue(userJson, UserDTOByManager.class);
            } else {
                userDTO = userService.getUserById(userId);
                if (userDTO == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseData(404, false, "User with ID: " + userId + " not found", null, null, null));
                }
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(imageFile);
                userDTO.setAvatar_url(imageUrl);
            }

            UserDTOByManager updatedUser = userService.updateUserByManager(userId, userDTO);
            return ResponseEntity.ok(new ResponseData(200, true, "User updated", updatedUser, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Fail to update user with ID: " + userId, null, null, null));
        }
    }

    // Delete user
    @Operation(summary = "Delete a user by ID", description = "Remove a user from the system using their ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
