package org.elevate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.elevate.exceptions.*;
import org.elevate.models.RequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.elevate.dtos.ClubRequestDTO;
import org.elevate.dtos.ApiResponseDTO;
import org.elevate.models.ClubRequest;
import org.elevate.services.ClubRequestService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club Request APIs", description = "APIs for managing requests to join clubs, including submitting, retrieving, approving, and rejecting club requests.")
public class ClubRequestController {

    private final ClubRequestService clubRequestService;

    public ClubRequestController(ClubRequestService clubRequestService) {
        this.clubRequestService = clubRequestService;
    }

    /**
     * Creates a new club join request (Accessible by Students).
     *
     * @param userComment The club request details including user ID, club ID, and comment.
     * @return A response entity containing a success message and the created request.
     * @throws ClubNotFoundException If the specified club does not exist.
     * @throws ClubLimitExceededException If the user has reached the maximum number of clubs.
     * @throws RequestAlreadyExistsException If the user has already requested to join this club.
     */
    @PostMapping("/student/club-requests")
    @Operation(
        summary = "Create a Club Request",
        description = "Allows a student to request to join a specific club."
    )
    public ResponseEntity<ApiResponseDTO<ClubRequestDTO>> createClubRequest(
            @RequestParam Long userId,
            @RequestParam Long clubId,
            @RequestParam String userComment
    ) throws ClubNotFoundException, ClubLimitExceededException, RequestAlreadyExistsException, InvalidRequestException {

        ClubRequest savedRequest = clubRequestService.createClubRequest(userId, clubId, userComment);

        ClubRequestDTO responseDTO = new ClubRequestDTO(savedRequest.getUser().getUserId(),
                savedRequest.getClub().getClubId(),
                savedRequest.getUserComment(),
                savedRequest.getStatus());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Club request submitted successfully", responseDTO));
    }

    /**
     * Retrieves all pending club requests for a specific club (Accessible by Club Admins & Super Admins).
     *
     * @param clubId The unique ID of the club whose requests need to be fetched.
     * @return A response entity containing a list of club requests.
     */
    @GetMapping("/clubadmin/club-requests/{clubId}")
    @Operation(
        summary = "Get Club Requests by Club ID",
        description = "Fetches all pending club requests for a specific club (Club Admin and Super Admin only)."
    )
    public ResponseEntity<ApiResponseDTO<List<ClubRequest>>> getClubRequests(
        @Parameter(description = "The unique ID of the club whose requests need to be retrieved", required = true, example = "1")
        @PathVariable Long clubId,
        @RequestParam RequestStatus status
    ) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByClubId(clubId, status);

        return ResponseEntity.ok(new ApiResponseDTO<>("Club requests fetched successfully", requests));
    }

    /**
     * Retrieves all club requests made by a specific user (Accessible by Students).
     *
     * @param userId The unique ID of the user whose club requests need to be fetched.
     * @return A response entity containing a list of club requests made by the user.
     */
    @GetMapping("/student/club-requests/user/{userId}")
    @Operation(
        summary = "Get Club Requests by User ID",
        description = "Fetches all club requests made by a specific user."
    )
    public ResponseEntity<ApiResponseDTO<List<ClubRequest>>> getClubRequestsByUser(
        @Parameter(description = "The unique ID of the user whose club requests need to be retrieved", required = true, example = "1")
        @PathVariable Long userId,
        @RequestParam RequestStatus status
    ) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByUserId(userId, status);

        return ResponseEntity.ok(new ApiResponseDTO<>("Club requests fetched successfully", requests));
    }

    @PatchMapping("/clubadmin/club-requests/{requestId}/update")
    @Operation(
            summary = "Modify a Club Request",
            description = "Allows an admin to change the status of a request or alter comments"
    )
    public ResponseEntity<ApiResponseDTO> updateClubRequest(
            @PathVariable Long requestId,
            @RequestParam Map<String, String> updates
    ) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        ClubRequest response = clubRequestService.patchUpdates(requestId, updates);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request updated successfully", response));
    }
}
