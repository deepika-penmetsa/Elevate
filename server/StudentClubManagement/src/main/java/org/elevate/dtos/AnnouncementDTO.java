package org.elevate.dtos;

import org.springframework.web.multipart.MultipartFile;
import org.elevate.models.AnnouncementType;

public class AnnouncementDTO {
    private Long postedById;
    private Long clubId;
    private String title;
    private String content;
    private AnnouncementType type;
    private MultipartFile announcementImage;
    private String announcementImageUrl;

    public AnnouncementDTO() {}

    public AnnouncementDTO(Long postedById, Long clubId, String title, String content, AnnouncementType type,
                           MultipartFile announcementImage, String announcementImageUrl) {
        this.postedById = postedById;
        this.clubId = clubId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.announcementImage = announcementImage;
        this.announcementImageUrl = announcementImageUrl;
    }

    // Getters and Setters

    public Long getPostedById() {
        return postedById;
    }

    public void setPostedById(Long postedById) {
        this.postedById = postedById;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
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

    public MultipartFile getAnnouncementImage() {
        return announcementImage;
    }

    public void setAnnouncementImage(MultipartFile announcementImage) {
        this.announcementImage = announcementImage;
    }

    public String getAnnouncementImageUrl() {
        return announcementImageUrl;
    }

    public void setAnnouncementImageUrl(String announcementImageUrl) {
        this.announcementImageUrl = announcementImageUrl;
    }
}
