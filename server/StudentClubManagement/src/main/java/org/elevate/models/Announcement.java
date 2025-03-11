package org.elevate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonIgnore
    private Club club;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 1000, message = "Content must be less than 1000 characters")
    private String content;

    @NotNull(message = "Announcement type is mandatory")
    @Enumerated(EnumType.STRING)
    private AnnouncementType type;

    @Lob
    @Column(name = "announcement_image", columnDefinition = "LONGBLOB")
    private byte[] announcementImage;

    @JsonIgnore
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    private Set<UserAnnouncement> userAnnouncements;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AnnouncementType getType() {
        return type;
    }

    public void setType(AnnouncementType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public byte[] getAnnouncementImage() {
        return announcementImage;
    }

    public void setAnnouncementImage(byte[] announcementImage) {
        this.announcementImage = announcementImage;
    }

    public Set<UserAnnouncement> getUserAnnouncements() {
        return userAnnouncements;
    }

    public void setUserAnnouncements(Set<UserAnnouncement> userAnnouncements) {
        this.userAnnouncements = userAnnouncements;
    }
}
