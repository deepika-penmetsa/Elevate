package org.elevate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.elevate.dtos.AnnouncementDTO;
import org.elevate.dtos.AnnouncementResponseDTO;
import org.elevate.dtos.ApiResponseDTO;
import org.elevate.exceptions.ClubNotFoundException;
import org.elevate.exceptions.UnauthorizedActionException;
import org.elevate.models.Announcement;
import org.elevate.models.AnnouncementType;
import org.elevate.services.AnnouncementService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Announcement APIs", description = "APIs for managing club announcements, including creation and retrieval with filters.")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * Creates a new announcement for a club (Accessible by Club Admins & Super Admins).
     */
    @PostMapping(value = "/clubadmin/announcements", consumes = {"multipart/form-data"})
    @Operation(summary = "Create an Announcement", description = "Allows club admins to create an announcement for their respective clubs.")
    public ResponseEntity<ApiResponseDTO<Announcement>> createAnnouncement(
            @RequestParam("postedById") Long postedById,
            @RequestParam("clubId") Long clubId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("type") AnnouncementType type,
            @RequestParam(value = "announcementImage", required = false) MultipartFile announcementImage
    ) throws ClubNotFoundException, UnauthorizedActionException {
        AnnouncementDTO announcementDTO = new AnnouncementDTO();
        announcementDTO.setPostedById(postedById);
        announcementDTO.setClubId(clubId);
        announcementDTO.setTitle(title);
        announcementDTO.setContent(content);
        announcementDTO.setType(type);
        announcementDTO.setAnnouncementImage(announcementImage);

        Announcement announcement = announcementService.createAnnouncement(announcementDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Announcement posted successfully", announcement));
    }

    /**
     * Retrieves paginated announcements by type (Events or General Announcements).
     */
    @GetMapping("/student/announcements/{userId}/club/{clubId}")
    @Operation(summary = "Get Announcements by Type", description = "Fetches paginated announcements for a specific club and type.")
    public ResponseEntity<ApiResponseDTO<Page<AnnouncementResponseDTO>>> getAnnouncementsByType(
            @PathVariable Long userId,
            @PathVariable Long clubId,
            @RequestParam("type") AnnouncementType type,
            Pageable pageable
    ) {
        Page<AnnouncementResponseDTO> announcements = announcementService.getAnnouncementsByType(clubId, userId, type, pageable);
        return ResponseEntity.ok(new ApiResponseDTO<>("Announcements retrieved successfully", announcements));
    }

    /**
     * Retrieves paginated unseen announcements by type (Events or General Announcements).
     */
    @GetMapping("/student/announcements/{userId}/club/{clubId}/unseen")
    @Operation(summary = "Get Unseen Announcements by Type", description = "Fetches paginated unseen announcements for a specific club and type.")
    public ResponseEntity<ApiResponseDTO<Page<AnnouncementResponseDTO>>> getUnseenAnnouncementsByType(
            @PathVariable Long userId,
            @PathVariable Long clubId,
            @RequestParam("type") AnnouncementType type,
            Pageable pageable
    ) {

        Page<AnnouncementResponseDTO> unseenAnnouncements = announcementService.getUnseenAnnouncementsByType(clubId, userId, type, pageable);
        return ResponseEntity.ok(new ApiResponseDTO<>("Unseen announcements retrieved successfully", unseenAnnouncements));
    }
}