package org.elevate.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.elevate.exceptions.RecordNotFoundException;
import org.elevate.models.UserAnnouncement;
import org.elevate.repositories.UserAnnouncementRepository;

@Service
public class UserAnnouncementService {

    private final UserAnnouncementRepository userAnnouncementRepository;

    public UserAnnouncementService(UserAnnouncementRepository userAnnouncementRepository) {
        this.userAnnouncementRepository = userAnnouncementRepository;
    }


    /**
     * Marks an announcement as seen by a specific user.
     *
     * @param userId The ID of the user.
     * @param announcementId The ID of the announcement.
     */
    @Transactional
    public void markAnnouncementAsSeen(Long userId, Long announcementId) throws RecordNotFoundException {
        UserAnnouncement userAnnouncement = userAnnouncementRepository
                .findByUser_UserIdAndAnnouncement_Id(userId, announcementId)
                .orElseThrow(() -> new RecordNotFoundException("User announcement record not found."));

        userAnnouncement.setSeen(true);
        userAnnouncementRepository.save(userAnnouncement);
    }
}
