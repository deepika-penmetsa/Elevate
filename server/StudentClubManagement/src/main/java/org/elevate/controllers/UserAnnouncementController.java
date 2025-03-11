package org.elevate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.elevate.dtos.ApiResponseDTO;
import org.elevate.exceptions.RecordNotFoundException;
import org.elevate.services.UserAnnouncementService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user/announcements")
@Tag(name = "User Announcements API", description = "APIs for marking announcements as seen.")
public class UserAnnouncementController {

    @Autowired
    private UserAnnouncementService userAnnouncementService;

    /**
     * Marks an announcement as seen by a user.
     *
     * @param userId The ID of the user.
     * @param announcementId The ID of the announcement.
     * @return Response indicating success.
     */
    @PatchMapping("/{userId}/{announcementId}/mark-seen")
    @Operation(summary = "Mark Announcement as Seen", description = "Marks a specific announcement as seen for the user.")
    public ResponseEntity<ApiResponseDTO<String>> markAnnouncementAsSeen(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "Announcement ID", required = true, example = "10") @PathVariable Long announcementId) throws RecordNotFoundException {

        userAnnouncementService.markAnnouncementAsSeen(userId, announcementId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Announcement marked as seen.", null));
    }
}
