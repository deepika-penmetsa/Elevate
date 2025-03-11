package org.elevate.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAnnouncementId implements Serializable {

    private Long userId;
    private Long announcementId;

    public UserAnnouncementId() {}

    public UserAnnouncementId(Long userId, Long announcementId) {
        this.userId = userId;
        this.announcementId = announcementId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserAnnouncementId that = (UserAnnouncementId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(announcementId, that.announcementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, announcementId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }
}
