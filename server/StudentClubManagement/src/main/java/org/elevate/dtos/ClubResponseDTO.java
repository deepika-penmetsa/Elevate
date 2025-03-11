package org.elevate.dtos;

import org.springframework.web.multipart.MultipartFile;

public class ClubResponseDTO {

    private Long clubId;

    private String clubName;

    private String description;

    private int noOfMembers;  // Default: 0 members while creating

    private int availableSlots;  // Default: totalSlots - noOfMembers

    private int totalSlots; // Default: 20 while creating a club

    private Long adminId; // Club Admin will be assigned later

    private MultipartFile clubImage;
    private MultipartFile clubBackgroundImage;

    private String clubImageUrl; // Base64 encoded string or image URL

    private String clubBackgroundImageUrl; // Base64 encoded string or image URL

    /**
     * Default constructor to initialize default values.
     */
    public ClubResponseDTO() {
        this.noOfMembers = 0;
        this.totalSlots = 20;
        this.availableSlots = this.totalSlots;
        this.adminId = null; // Initially, no club admin
    }

    /**
     * Constructor for setting initial values while creating a club.
     */
    public ClubResponseDTO(String clubName, String description) {
        this.clubName = clubName;
        this.description = description;
        this.noOfMembers = 0;
        this.totalSlots = 20;
        this.availableSlots = this.totalSlots;
        this.adminId = null; // Initially, no club admin
    }

    // Getters and Setters


    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getNoOfMembers() { return noOfMembers; }
    public void setNoOfMembers(int noOfMembers) { this.noOfMembers = noOfMembers; }

    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }

    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots - this.noOfMembers; // Adjust available slots dynamically
    }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public MultipartFile getClubImage() {
        return clubImage;
    }

    public void setClubImage(MultipartFile clubImage) {
        this.clubImage = clubImage;
    }

    public MultipartFile getClubBackgroundImage() {
        return clubBackgroundImage;
    }

    public void setClubBackgroundImage(MultipartFile clubBackgroundImage) {
        this.clubBackgroundImage = clubBackgroundImage;
    }

    public String getClubImageUrl() { return clubImageUrl; }
    public void setClubImageUrl(String clubImageUrl) { this.clubImageUrl = clubImageUrl; }

    public String getClubBackgroundImageUrl() { return clubBackgroundImageUrl; }
    public void setClubBackgroundImageUrl(String clubBackgroundImageUrl) { this.clubBackgroundImageUrl = clubBackgroundImageUrl; }
}
