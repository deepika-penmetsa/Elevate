package org.elevate.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserAnnouncement {

    @EmbeddedId
    private UserAnnouncementId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("announcementId")
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    private boolean isSeen = false;
    private LocalDateTime seenAt;

    public UserAnnouncement() {} // Default constructor required for JPA

    public UserAnnouncement(User user, Announcement announcement) {
        this.id = new UserAnnouncementId(user.getUserId(), announcement.getId()); // ✅ Ensure ID is set
        this.user = user;
        this.announcement = announcement;
        this.isSeen = false;
    }

    @PrePersist
    @PreUpdate
    protected void updateSeenTimestamp() {
        if (isSeen) {
            this.seenAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public UserAnnouncementId getId() {
        return id;
    }

    public void setId(UserAnnouncementId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (this.announcement != null) {
            this.id = new UserAnnouncementId(user.getUserId(), this.announcement.getId()); // ✅ Ensure ID updates
        }
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
        if (this.user != null) {
            this.id = new UserAnnouncementId(this.user.getUserId(), announcement.getId()); // ✅ Ensure ID updates
        }
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        this.isSeen = seen;
    }

    public LocalDateTime getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(LocalDateTime seenAt) {
        this.seenAt = seenAt;
    }
}
