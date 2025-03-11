package org.elevate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.elevate.dtos.*;
import org.elevate.models.Role;
import org.elevate.models.User;
import org.elevate.services.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User APIs", description = "APIs for managing user information, including retrieval, creation, updating, and deletion of users.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    @Operation(summary = "Get All Users", description = "Fetches a list of all registered users.")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponseDTO<>("Users retrieved successfully", userService.getAllUsers()));
    }

    @GetMapping("/admin/users/{id}")
    @Operation(summary = "Get User by ID", description = "Fetches user details by their unique ID.")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
        @Parameter(description = "The unique ID of the user", required = true, example = "1")
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>("User retrieved", userService.getUserById(id)));
    }

    @GetMapping("/student/users/email")
    @Operation(summary = "Get Users by Email Prefix", description = "Fetches users whose email starts with the given prefix.")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getUserByEmailPrefix(
        @Parameter(description = "Email prefix to search for", required = true, example = "john")
        @RequestParam String email
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>("Users retrieved", userService.getAllUsersStartingWithEmail(email)));
    }

    /**
     * **Registers a new user with optional profile photo.**
     * - Accepts `profilePhoto` as `MultipartFile`.
     */
    @PostMapping(value = "/auth/users/signup", consumes = {"multipart/form-data"})
    @Operation(summary = "Register a New User", description = "Allows students to sign up and create an account.")
    public ResponseEntity<ApiResponseDTO> createUser(
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam(value = "phone", required = false) String phone,
        @RequestParam(value = "street", required = false) String street,
        @RequestParam(value = "apartment", required = false) String apartment,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "state", required = false) String state,
        @RequestParam(value = "zipcode", required = false) String zipcode,
        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "bio", required = false) String bio,
        @RequestParam Role role,
        @RequestParam(value = "birthday", required = false) String birthdayStr,
        @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto
    ) throws ParseException {

        UserDTO userDTO = setUserFromUserParams(firstName, lastName, email, password, phone, street, apartment, city, state, zipcode, country, bio, role, birthdayStr);
        User user = userService.createUser(userDTO, profilePhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("User Registered Successfully", user));
    }

    private UserDTO setUserFromUserParams(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password, @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "street", required = false) String street, @RequestParam(value = "apartment", required = false) String apartment, @RequestParam(value = "city", required = false) String city, @RequestParam(value = "state", required = false) String state, @RequestParam(value = "zipcode", required = false) String zipcode, @RequestParam(value = "country", required = false) String country, @RequestParam(value = "bio", required = false) String bio, @RequestParam Role role, @RequestParam(value = "birthday", required = false) String birthdayStr) throws ParseException {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setPhone(phone);
        userDTO.setStreet(street);
        userDTO.setApartment(apartment);
        userDTO.setCity(city);
        userDTO.setState(state);
        userDTO.setZipcode(zipcode);
        userDTO.setCountry(country);
        userDTO.setBio(bio);
        userDTO.setRole(role);
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = formatter.parse(birthdayStr);
            userDTO.setBirthday(birthday);
        }
        return userDTO;
    }


    /**
     * **Updates user profile with optional profile photo.**
     */
    @PutMapping(value = "/student/profile/{id}/update", consumes = {"multipart/form-data"})
    @Operation(summary = "Update User Profile", description = "Allows students, club admins, and super admins to update their profile details.")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserProfile(
        @Parameter(description = "The user ID", required = true, example = "1") @PathVariable Long id,
        @RequestParam(value = "firstName", required = false) String firstName,
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "phone", required = false) String phone,
        @RequestParam(value = "street", required = false) String street,
        @RequestParam(value = "apartment", required = false) String apartment,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "state", required = false) String state,
        @RequestParam(value = "zipcode", required = false) String zipcode,
        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "bio", required = false) String bio,
        @RequestParam(value = "birthday", required = false) String birthdayStr,
        @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto
    ) throws ParseException {
        UpdateUserDTO updatedUserDTO = new UpdateUserDTO();
        updatedUserDTO.setFirstName(firstName);
        updatedUserDTO.setLastName(lastName);
        updatedUserDTO.setPhone(phone);
        updatedUserDTO.setStreet(street);
        updatedUserDTO.setApartment(apartment);
        updatedUserDTO.setCity(city);
        updatedUserDTO.setState(state);
        updatedUserDTO.setZipcode(zipcode);
        updatedUserDTO.setCountry(country);
        updatedUserDTO.setBio(bio);

        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = formatter.parse(birthdayStr);
            updatedUserDTO.setBirthday(birthday);
        }

        return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", userService.updateUser(id, updatedUserDTO, profilePhoto)));
    }


    @PutMapping(value = "/admin/users/{id}/update", consumes = {"multipart/form-data"})
    @Operation(summary = "Admin: Update User Information", description = "Allows an admin to update an existing user's details.")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserByAdmin(
        @Parameter(description = "The user ID", required = true, example = "1") @PathVariable Long id,
        @RequestParam(value = "firstName", required = false) String firstName,
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "password", required = false) String password,
        @RequestParam(value = "phone", required = false) String phone,
        @RequestParam(value = "street", required = false) String street,
        @RequestParam(value = "apartment", required = false) String apartment,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "state", required = false) String state,
        @RequestParam(value = "zipcode", required = false) String zipcode,
        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "bio", required = false) String bio,
        @RequestParam(value = "role", required = false) Role role,
        @RequestParam(value = "birthday", required = false) String birthdayStr,
        @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto
    ) throws ParseException {

        UserDTO userDTO = setUserFromUserParams(firstName, lastName, email, password, phone, street, apartment, city, state, zipcode, country, bio, role, birthdayStr);
        return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", userService.updateUserFromUserDTO(id, userDTO, profilePhoto)));
    }


    /**
     * **Partially updates a user profile, including profile photo.**
     * - Accepts `profilePhoto` as `MultipartFile`.
     * - Accepts a `Map<String, String>` of updates for specific fields.
     */
    @PatchMapping(value = "/student/profile/{id}", consumes = {"multipart/form-data"})
    @Operation(summary = "Partially Update User Profile", description = "Allows updating specific fields or profile photo without modifying other details.")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> patchUser(
        @Parameter(description = "The user ID", required = true, example = "1") @PathVariable Long id,
        @RequestParam Map<String, String> updates,
        @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto

    ){
        return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", userService.patchUser(id, updates, profilePhoto)));
    }


    /**
     * **Partially updates a user profile**
     * - Accepts `profilePhoto` as `MultipartFile`.
     */
    @PatchMapping(value = "/student/update/profile-photo/{id}", consumes = {"multipart/form-data"})
    @Operation(summary = "Update User Profile Photo", description = "Allows to update user profile photo without modifying other details.")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> patchUser(
        @Parameter(description = "The user ID", required = true, example = "1") @PathVariable Long id,
        @RequestParam MultipartFile profilePhoto
    ){
        return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", userService.patchUserProfilePhoto(id, profilePhoto)));
    }

    @DeleteMapping("/admin/users/{id}")
    @Operation(summary = "Delete a User", description = "Allows super admins to delete a user from the system.")
    public ResponseEntity<ApiResponseDTO<String>> deleteUser(
        @Parameter(description = "The user ID", required = true, example = "1") @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponseDTO<>("User deleted successfully", null));
    }
}