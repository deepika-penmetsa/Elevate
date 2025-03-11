package org.elevate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @NotBlank(message = "Club name is mandatory")
    @Size(max = 100, message = "Club name must be less than 100 characters")
    @Column(unique = true)
    private String clubName;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;

    private int noOfMembers; // Default: 0 (Assigned dynamically)

    private int availableSlots; // Default: totalSlots - noOfMembers (Assigned dynamically)

    private int totalSlots; // Default: 20 (Assigned dynamically)

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "club_admin_user_id")
    private User clubAdmin;

    @Lob
    @Column(name = "club_image", columnDefinition = "LONGBLOB")
    private byte[] clubImage;

    @Lob
    @Column(name = "club_background_image", columnDefinition = "LONGBLOB")
    private byte[] clubBackgroundImage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserClub> userClubs;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Announcement> announcements;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubRequest> requests;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubAdminRequest> clubAdminRequests;

    /**
     * Ensures default values for `availableSlots`, `totalSlots`, and `noOfMembers` when creating a club.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Set default values if not provided
        if (this.totalSlots <= 0) {
            this.totalSlots = 20; // Default total slots
        }
        if (this.noOfMembers < 0) {
            this.noOfMembers = 0; // Default noOfMembers
        }

        // Automatically adjust availableSlots
        this.availableSlots = this.totalSlots - this.noOfMembers;
    }

    /**
     * Ensures data consistency when updating a club.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        // Prevent reducing totalSlots below noOfMembers
        if (this.totalSlots < this.noOfMembers) {
            throw new IllegalArgumentException("Total slots cannot be less than the number of current members.");
        }

        // Ensure available slots remain consistent
        this.availableSlots = this.totalSlots - this.noOfMembers;
    }

    // Getters and Setters

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNoOfMembers() {
        return noOfMembers;
    }

    public void setNoOfMembers(int noOfMembers) {
        if (noOfMembers < 0) {
            throw new IllegalArgumentException("Number of members cannot be negative.");
        }
        this.noOfMembers = noOfMembers;
        this.availableSlots = this.totalSlots - noOfMembers; // Adjust available slots dynamically
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        if (availableSlots > this.totalSlots) {
            throw new IllegalArgumentException("Available slots cannot exceed total slots.");
        }
        this.availableSlots = availableSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        if (totalSlots < this.noOfMembers) {
            throw new IllegalArgumentException("Total slots cannot be less than the number of current members.");
        }
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots - this.noOfMembers; // Adjust available slots dynamically
    }

    public User getClubAdmin() {
        return clubAdmin;
    }

    public void setClubAdmin(User clubAdmin) {
        this.clubAdmin = clubAdmin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public byte[] getClubImage() {
        return clubImage;
    }

    public void setClubImage(byte[] clubImage) {
        this.clubImage = clubImage;
    }

    public byte[] getClubBackgroundImage() {
        return clubBackgroundImage;
    }

    public void setClubBackgroundImage(byte[] clubBackgroundImage) {
        this.clubBackgroundImage = clubBackgroundImage;
    }
}
