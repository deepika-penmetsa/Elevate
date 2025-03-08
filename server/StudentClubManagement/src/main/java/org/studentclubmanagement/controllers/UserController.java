package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.*;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.services.UserService;
import org.studentclubmanagement.exceptions.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User APIs", description = "APIs for managing user information, including retrieval, creation, updating, and deletion of users.")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all users (Accessible by Super Admins).
     *
     * @return A list of all users.
     */
    @GetMapping("/admin/users")
    @Operation(
        summary = "Get All Users",
        description = "Fetches a list of all registered users."
    )
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponseDTO<>("Successfully retrieved all users", users));
    }

    /**
     * Retrieves a user by ID (Accessible by Super Admins).
     *
     * @param id The unique ID of the user.
     * @return The user's details.
     */
    @GetMapping("/admin/users/{id}")
    @Operation(
        summary = "Get User by ID",
        description = "Fetches user details by their unique ID."
    )
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
        @Parameter(description = "The unique ID of the user", required = true, example = "1")
        @PathVariable Long id
    ) {
        try {
            UserResponseDTO user = userService.getUserInUserResponseDTO(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("User retrieved successfully", user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>("User not found", null));
        }
    }

    /**
     * Retrieves users whose email starts with the given prefix (Accessible by Students, Club Admins, and Super Admins).
     *
     * @param email The email prefix.
     * @return A list of users matching the email prefix.
     */
    @GetMapping("/student/users/email")
    @Operation(
        summary = "Get Users by Email Prefix",
        description = "Fetches users whose email starts with the given prefix."
    )
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getUserByEmail(
        @Parameter(description = "Email prefix to search for", required = true, example = "john")
        @RequestParam String email
    ) {
        List<UserResponseDTO> users = userService.getAllUsersStartingWithEmail(email);
        return ResponseEntity.ok(new ApiResponseDTO<>("Successfully retrieved all users", users));
    }

    /**
     * Registers a new user (Signup) (Accessible by Students).
     *
     * @param userDTO The user details for registration.
     * @return The newly created user's details.
     */
    @PostMapping("/student/register")
    @Operation(
        summary = "Register a New User",
        description = "Allows students to sign up and create an account."
    )
    public ResponseEntity<ApiResponseDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        try {
            User savedUser = userService.createUserFromDTO(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("User Registered Successfully", savedUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDTO("User Already Exists [CHECK EMAIL & USER ID]", null));
        }
    }

    /**
     * Updates user profile (Accessible by Students, Club Admins, and Super Admins).
     *
     * @param id The user ID.
     * @param updatedUserDTO The updated user details.
     * @return The updated user.
     */
    @PutMapping("/student/profile/{id}/update")
    @Operation(
        summary = "Update User Profile",
        description = "Allows students, club admins, and super admins to update their profile details."
    )
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserProfile(
        @Parameter(description = "The user ID", required = true, example = "1")
        @PathVariable Long id,
        @Validated @RequestBody UpdateUserDTO updatedUserDTO
    ) {
        try {
            UserResponseDTO savedUser = userService.updateUserFromDTO(id, updatedUserDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("User profile updated successfully", savedUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>("Error: User not found", null));
        }
    }

    /**
     * Updates a user (Admin action) (Accessible by Super Admins).
     *
     * @param id The user ID.
     * @param userDTO The updated user details.
     * @return The updated user.
     */
    @PutMapping("/admin/users/{id}/update")
    @Operation(
        summary = "Admin: Update User Information",
        description = "Allows an admin to update an existing user's details."
    )
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserByAdmin(
        @Parameter(description = "The user ID", required = true, example = "1")
        @PathVariable Long id,
        @Validated @RequestBody UserDTO userDTO
    ) {
        try {
            UserResponseDTO savedUser = userService.updateUserFromUserDTO(id, userDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", savedUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>("Error: User not found", null));
        }
    }

    /**
     * Deletes a user (Accessible by Super Admins).
     *
     * @param id The user ID.
     * @return A success or error message.
     */
    @DeleteMapping("/admin/users/{id}")
    @Operation(
        summary = "Delete a User",
        description = "Allows super admins to delete a user from the system."
    )
    public ResponseEntity<ApiResponseDTO<String>> deleteUser(
        @Parameter(description = "The user ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("User deleted successfully", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>("Error: User not found", null));
        }
    }
}
