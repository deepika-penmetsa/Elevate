package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.bytebuddy.build.Plugin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.ClubRequestDTO;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.exceptions.ClubCapacityExceededException;
import org.studentclubmanagement.exceptions.ClubLimitExceededException;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.RequestAlreadyExistsException;
import org.studentclubmanagement.models.ClubRequest;
import org.studentclubmanagement.services.ClubRequestService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @param clubRequestDTO The club request details including user ID, club ID, and comment.
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
    public ResponseEntity<ApiResponseDTO<ClubRequestDTO>> createClubRequest(@RequestBody ClubRequestDTO clubRequestDTO)
            throws ClubNotFoundException, ClubLimitExceededException, RequestAlreadyExistsException {

        ClubRequest savedRequest = clubRequestService.createClubRequest(clubRequestDTO);

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
    public ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getClubRequests(
        @Parameter(description = "The unique ID of the club whose requests need to be retrieved", required = true, example = "1")
        @PathVariable Long clubId
    ) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByClubId(clubId);

        return ResponseEntity.ok(new ApiResponseDTO<>("Club requests fetched successfully", convertToResponseDTOs(requests)));
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
    public ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getClubRequestsByUser(
        @Parameter(description = "The unique ID of the user whose club requests need to be retrieved", required = true, example = "10")
        @PathVariable Long userId
    ) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByUserId(userId);

        return ResponseEntity.ok(new ApiResponseDTO<>("Club requests fetched successfully", convertToResponseDTOs(requests)));
    }

    private List<ClubRequestDTO> convertToResponseDTOs(List<ClubRequest> requests) {
        return requests.stream()
                .map(req -> new ClubRequestDTO(req.getUser().getUserId(),
                        req.getClub().getClubName(),
                        req.getClub().getClubId(),
                        req.getUserComment(),
                        req.getApproverComment(),
                        req.getStatus(),
                        req.getCreatedAt(),
                        req.getUpdatedAt()))
                .collect(Collectors.toList());
    }

//    /**
//     * Approves a club request (Accessible by Club Admins & Super Admins).
//     *
//     * @param requestId The unique ID of the club request to approve.
//     * @return A success message indicating the approval of the club request.
//     * @throws ClubLimitExceededException If the user has already joined the maximum allowed clubs.
//     * @throws ClubCapacityExceededException If the club has no available slots.
//     * @throws RequestAlreadyExistsException If the user is already a member of the club.
//     */
//    @PutMapping("/clubadmin/club-requests/{requestId}/approve")
//    @Operation(
//        summary = "Approve a Club Request",
//        description = "Allows an admin to approve a club join request."
//    )
//    public ResponseEntity<ApiResponseDTO> approveClubRequest(
//        @Parameter(description = "The unique ID of the club request to approve", required = true, example = "5")
//        @PathVariable Long requestId,
//        @RequestParam String approverComment
//    ) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
//        clubRequestService.approveClubRequest(requestId, approverComment);
//        return ResponseEntity.ok(new ApiResponseDTO<>("Club request approved successfully", null));
//    }
//
//    /**
//     * Rejects a club request (Accessible by Club Admins & Super Admins).
//     *
//     * @param requestId The unique ID of the club request to reject.
//     * @return A success message indicating the rejection of the club request.
//     */
//    @PutMapping("/clubadmin/club-requests/{requestId}/reject")
//    @Operation(
//        summary = "Reject a Club Request",
//        description = "Allows an admin to reject a club join request."
//    )
//    public ResponseEntity<ApiResponseDTO> rejectClubRequest(
//        @Parameter(description = "The unique ID of the club request to reject", required = true, example = "5")
//        @PathVariable Long requestId,
//        @RequestParam String approverComment
//    ) {
//        clubRequestService.rejectClubRequest(requestId, approverComment);
//        return ResponseEntity.ok(new ApiResponseDTO<>("Club request rejected successfully", null));
//    }

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
