package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.elevate.models.UserAnnouncement;

import java.util.Optional;

@Repository
public interface UserAnnouncementRepository extends JpaRepository<UserAnnouncement, Long> {

    /**
     * Find a specific UserAnnouncement by userId and announcementId.
     * @param userId The ID of the user.
     * @param announcementId The ID of the announcement.
     * @return Optional UserAnnouncement entry.
     */
    Optional<UserAnnouncement> findByUser_UserIdAndAnnouncement_Id(Long userId, Long announcementId);
}
