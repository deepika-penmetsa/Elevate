package org.elevate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.elevate.dtos.ClubResponseDTO;
import org.elevate.dtos.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.elevate.dtos.ApiResponseDTO;
import org.elevate.models.UserClub;
import org.elevate.services.UserClubService;

import java.util.List;

@RestController
@RequestMapping("/student/user-clubs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User Club APIs", description = "APIs for managing user club memberships, allowing users to fetch joined clubs.")
public class UserClubController {

    private final UserClubService userClubService;

    public UserClubController(UserClubService userClubService) {
        this.userClubService = userClubService;
    }

    /**
     * Retrieves all clubs that a user has joined (Accessible by Students, Club Admins, and Super Admins).
     *
     * @param userId The unique ID of the user whose joined clubs need to be fetched.
     * @return A response entity containing a list of clubs the user is a member of.
     */
    @GetMapping("/{userId}")
    @Operation(
        summary = "Get Clubs Joined by User",
        description = "Fetches all clubs that a specific user has joined."
    )
    public ResponseEntity<ApiResponseDTO<List<ClubResponseDTO>>> getUserClubs(
        @Parameter(description = "The unique ID of the user whose clubs need to be retrieved", required = true, example = "10")
        @PathVariable Long userId
    ) {
        List<ClubResponseDTO> userClubs = userClubService.getUserClubs(userId);
        return ResponseEntity.ok(new ApiResponseDTO<>("User clubs fetched successfully", userClubs));
    }

    /**
     * Retrieves all members of a club (Accessible by Students, Club Admins, and Super Admins).
     *
     * @param clubId The unique ID of the club whose joined members need to be fetched.
     * @return A response entity containing a list of members of a club.
     */
    @GetMapping("/{clubId}/club-members")
    @Operation(
        summary = "Get All Users of a club",
        description = "Fetches all members of a specific club."
    )
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getUsersOfAClub(
        @Parameter(description = "The unique ID of the club whose users need to be retrieved", required = true, example = "7")
        @PathVariable Long clubId
    ) {
        List<UserResponseDTO> userClubs = userClubService.getUsersOfAClub(clubId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club members fetched successfully", userClubs));
    }
}
