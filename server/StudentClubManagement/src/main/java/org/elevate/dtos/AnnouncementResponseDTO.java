package org.elevate.dtos;

import org.elevate.models.AnnouncementType;

import java.time.LocalDateTime;

public class AnnouncementResponseDTO {
    private Long id;
    private String title;
    private String content;
    private AnnouncementType type;
    private LocalDateTime createdAt;
    private boolean isSeen;
    private String announcementImageUrl;

    public AnnouncementResponseDTO(Long id, String title, String content, AnnouncementType type,
                                   LocalDateTime createdAt, boolean isSeen, byte[] announcementImage) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
        this.isSeen = isSeen;

        // Convert image to Base64 format
        if (announcementImage != null) {
            this.announcementImageUrl = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(announcementImage);
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getAnnouncementImageUrl() {
        return announcementImageUrl;
    }

    public void setAnnouncementImageUrl(String announcementImageUrl) {
        this.announcementImageUrl = announcementImageUrl;
    }
}
