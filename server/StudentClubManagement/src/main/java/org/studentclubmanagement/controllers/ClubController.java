package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.ClubResponseDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.services.ClubService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club APIs", description = "APIs for managing and viewing clubs.")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping("/student/clubs")
    @Operation(summary = "Get All Clubs", description = "Fetches a list of all clubs available in the system.")
    public ResponseEntity<ApiResponseDTO<List<ClubResponseDTO>>> getAllClubs() {
        List<ClubResponseDTO> clubResponseDTOs = clubService.getAllClubs();
        return ResponseEntity.ok(new ApiResponseDTO<>("Clubs retrieved successfully", clubResponseDTOs));
    }

    @GetMapping("/admin/clubs/{id}")
    @Operation(summary = "Get Club by ID", description = "Fetches details of a specific club by its unique ID.")
    public ResponseEntity<ApiResponseDTO<ClubResponseDTO>> getClubById(
            @Parameter(description = "The unique ID of the club to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        try {
            ClubResponseDTO clubResponseDTO = clubService.getClubById(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club retrieved successfully", clubResponseDTO));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Club not found", null));
        }
    }

    @PostMapping(path = "/admin/clubs", consumes = {"multipart/form-data"})
    @Operation(summary = "Create a New Club", description = "Allows super admins to create a new club with optional admin assignment.")
    public ResponseEntity<ApiResponseDTO<ClubResponseDTO>> createClub(
            @RequestParam("clubName") String clubName,
            @RequestParam("description") String description,
            @RequestParam(value = "noOfMembers", required = false, defaultValue = "0") int noOfMembers,
            @RequestParam(value = "totalSlots", required = false, defaultValue = "20") int totalSlots,
            @RequestParam(value = "adminId", required = false) Long adminId,
            @RequestParam("clubImage") MultipartFile clubImage,
            @RequestParam("clubBackgroundImage") MultipartFile clubBackgroundImage) {

        int availableSlots = totalSlots - noOfMembers;
        ClubResponseDTO clubResponseDTO = new ClubResponseDTO();
        clubResponseDTO.setClubName(clubName);
        clubResponseDTO.setDescription(description);
        clubResponseDTO.setNoOfMembers(noOfMembers);
        clubResponseDTO.setAvailableSlots(availableSlots);
        clubResponseDTO.setTotalSlots(totalSlots);
        clubResponseDTO.setAdminId(adminId);
        clubResponseDTO.setClubImage(clubImage);
        clubResponseDTO.setClubBackgroundImage(clubBackgroundImage);

        ClubResponseDTO savedClubResponseDTO = clubService.createClub(clubResponseDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Club created successfully", savedClubResponseDTO));
    }

    @PutMapping(value = "/admin/clubs/{id}/update", consumes = {"multipart/form-data"})
    @Operation(summary = "Update an Existing Club", description = "Allows super admins to update club details and optionally assign an admin.")
    public ResponseEntity<ApiResponseDTO<ClubResponseDTO>> updateClub(
            @PathVariable Long id,
            @RequestParam("clubName") String clubName,
            @RequestParam("description") String description,
            @RequestParam(value = "totalSlots", required = false, defaultValue = "0") int totalSlots,
            @RequestParam(value = "adminId", required = false) Long adminId,
            @RequestParam(value = "clubImage", required = false) MultipartFile clubImage,
            @RequestParam(value = "clubBackgroundImage", required = false) MultipartFile clubBackgroundImage) throws ClubNotFoundException {

        ClubResponseDTO clubResponseDTO = new ClubResponseDTO();
        clubResponseDTO.setClubName(clubName);
        clubResponseDTO.setDescription(description);
        clubResponseDTO.setTotalSlots(totalSlots);
        clubResponseDTO.setAdminId(adminId);
        clubResponseDTO.setClubImage(clubImage);
        clubResponseDTO.setClubBackgroundImage(clubBackgroundImage);

        return ResponseEntity.ok(
                new ApiResponseDTO<>("Club updated successfully",
                        clubService.updateClub(id, clubResponseDTO, clubImage, clubBackgroundImage))
        );
    }

    @PatchMapping(value = "/admin/clubs/{id}", consumes = {"multipart/form-data"})
    @Operation(summary = "Partially Update a Club", description = "Allows updating specific fields of a club without modifying others.")
    public ResponseEntity<ApiResponseDTO<ClubResponseDTO>> patchClub(
            @PathVariable Long id,
            @RequestParam Map<String, String> updates,
            @RequestParam(value = "clubImage", required = false) MultipartFile clubImage,
            @RequestParam(value = "clubBackgroundImage", required = false) MultipartFile clubBackgroundImage) throws ClubNotFoundException {

        ClubResponseDTO updatedClub = clubService.patchClub(id, updates, clubImage, clubBackgroundImage);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club updated successfully", updatedClub));
    }

    @PatchMapping(value = "/admin/clubs/{id}/update-club-image", consumes = {"multipart/form-data"})
    @Operation(summary = "Update Club Image", description = "Allows updating or deleting club image without modifying other fields.")
    public ResponseEntity<ApiResponseDTO<ClubResponseDTO>> patchClubImage(
            @PathVariable Long id,
            @RequestParam(value = "clubImage", required = false) MultipartFile clubImage) throws ClubNotFoundException {

        ClubResponseDTO updatedClub = clubService.patchClubImage(id, clubImage);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club image updated successfully", updatedClub));
    }

    @PatchMapping(value = "/admin/clubs/{id}/update-club-background-image", consumes = {"multipart/form-data"})
    @Operation(summary = "Update Club Background Image", description = "Allows updating or deleting club background image without modifying other fields.")
    public ResponseEntity<ApiResponseDTO<ClubResponseDTO>> patchClubBackgroundImage(
            @PathVariable Long id,
            @RequestParam(value = "clubBackgroundImage", required = false) MultipartFile clubBackgroundImage) throws ClubNotFoundException {

        ClubResponseDTO updatedClub = clubService.patchClubBackgroundImage(id, clubBackgroundImage);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club Background Image updated successfully", updatedClub));
    }

    @DeleteMapping("/admin/clubs/{id}")
    @Operation(summary = "Delete a Club", description = "Allows super admins to delete a club from the system.")
    public ResponseEntity<ApiResponseDTO<String>> deleteClub(@PathVariable Long id) {
        try {
            clubService.deleteClub(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club with ID " + id + " has been successfully deleted.", null));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Error: Club with ID " + id + " not found.", null));
        }
    }
}
