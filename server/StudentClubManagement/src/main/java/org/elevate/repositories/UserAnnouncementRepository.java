package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.elevate.models.UserAnnouncement;

import java.util.Optional;

@Repository
public interface UserAnnouncementRepository extends JpaRepository<UserAnnouncement, Long> {

    /**
     * Find all UserAnnouncements by userId.
     * @param userId The ID of the user.
     * @return List of UserAnnouncement entries.
     */
//    List<UserAnnouncement> findByUser_UserId(Long userId);

    /**
     * Find a specific UserAnnouncement by userId and announcementId.
     * @param userId The ID of the user.
     * @param announcementId The ID of the announcement.
     * @return Optional UserAnnouncement entry.
     */
    Optional<UserAnnouncement> findByUser_UserIdAndAnnouncement_Id(Long userId, Long announcementId);
//
//    /**
//     * Find all UserAnnouncements by announcementId.
//     * @param announcementId The ID of the announcement.
//     * @return List of UserAnnouncement entries.
//     */
//    List<UserAnnouncement> findByAnnouncement_AnnouncementId(Long announcementId);
//
//    Optional<UserAnnouncement> findByUser_UserIdAndAnnouncement_Id(Long userId, Long id);
//
//    Page<UserAnnouncement> findByUser_UserIdAndAnnouncement_Club_ClubIdAndAnnouncement_TypeAndSeenFalse(Long userId, Long clubId, AnnouncementType type, Pageable pageable);
}
