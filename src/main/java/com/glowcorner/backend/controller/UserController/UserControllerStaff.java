package com.glowcorner.backend.controller.UserController;

import com.glowcorner.backend.model.DTO.User.UserDTOByCustomer;
import com.glowcorner.backend.model.DTO.User.UserDTOByStaff;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management System (Staff)", description = "Operations pertaining to users in the User Management System")
@RestController
@RequestMapping("/api/staff")
public class UserControllerStaff {

    private final UserService userService;

    public UserControllerStaff(UserService userService) {
        this.userService = userService;
    }

//    // Get all users
//    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
//    @GetMapping
//    public ResponseEntity<List<UserDTOByStaff>> getAllUsers() {
//        List<UserDTOByStaff> users = userService.getAllUsersByStaff();
//        return ResponseEntity.ok(users);
//    }

    // Get staff by ID
    @Operation(summary = "Get a user by ID", description = "Retrieve a single user using its ID")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseData> getUserById(@PathVariable String userId) {
        UserDTOByStaff user = userService.getStaffById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "User with ID: " + userId + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "User found", user, null, null));
    }

    // Update staff by ID
    @Operation(summary = "Update a staff by ID", description = "Update a staff using its ID")
    @PutMapping("{userId}")
    public ResponseEntity<ResponseData> updateUserByCustomer(@PathVariable String userId, @RequestBody UserDTOByStaff userDTO) {
        try {
            UserDTOByStaff updatedUser = userService.updateUserByStaff(userId, userDTO);
            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseData(404, false, "User with ID: " + userId + " not found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "User updated", updatedUser, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(400, false, e.getMessage(), null, null, null));
        }
    }
}