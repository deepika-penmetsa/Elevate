package org.elevate.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.elevate.dtos.AnnouncementDTO;
import org.elevate.dtos.AnnouncementResponseDTO;
import org.elevate.exceptions.ClubNotFoundException;
import org.elevate.exceptions.UserNotFoundException;
import org.elevate.exceptions.UnauthorizedActionException;
import org.elevate.models.*;
import org.elevate.repositories.*;

import java.io.IOException;
import java.util.*;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final UserAnnouncementRepository userAnnouncementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository, UserRepository userRepository, ClubRepository clubRepository,
                               UserClubRepository userClubRepository, UserAnnouncementRepository userAnnouncementRepository) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.userClubRepository = userClubRepository;
        this.userAnnouncementRepository = userAnnouncementRepository;
    }

    /**
     * Create an announcement with an optional image.
     * Super Admin can post in any club, Club Admin can post in their own club only.
     */
    @Transactional
    public Announcement createAnnouncement(AnnouncementDTO announcementDTO) throws UnauthorizedActionException, ClubNotFoundException {
        User user = userRepository.findById(announcementDTO.getPostedById())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(announcementDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        if (user.getRole() == Role.CLUB_ADMIN && !club.getClubAdmin().equals(user)) {
            throw new UnauthorizedActionException("Club Admin can only post announcements for their club.");
        }

        Announcement announcement = new Announcement();
        announcement.setPostedBy(user);
        announcement.setClub(club);
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setContent(announcementDTO.getContent());
        announcement.setType(announcementDTO.getType());

        MultipartFile announcementImage = announcementDTO.getAnnouncementImage();
        if (announcementImage != null && !announcementImage.isEmpty()) {
            try {
                announcement.setAnnouncementImage(announcementImage.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload announcement image", e);
            }
        }

        // Save Announcement
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Fetch all club members
        List<UserClub> clubMembers = userClubRepository.findByClub_ClubId(club.getClubId());

        // Create UserAnnouncement records for each user
        List<UserAnnouncement> userAnnouncements = new ArrayList<>();
        for (UserClub member : clubMembers) {
            UserAnnouncement userAnnouncement = new UserAnnouncement();
            userAnnouncement.setUser(member.getUser());
            userAnnouncement.setAnnouncement(savedAnnouncement);

            // Ensure composite key is correctly set before saving
            UserAnnouncementId userAnnouncementId = new UserAnnouncementId(member.getUser().getUserId(), savedAnnouncement.getId());
            userAnnouncement.setId(userAnnouncementId);

            // Club Admin should automatically see the announcement
            userAnnouncement.setSeen(member.getUser().getRole() == Role.CLUB_ADMIN);

            userAnnouncements.add(userAnnouncement);
        }

        if (!userAnnouncements.isEmpty()) {
            userAnnouncementRepository.saveAll(userAnnouncements);
        }

        return savedAnnouncement;
    }



    /**
     * Retrieves paginated announcements by type (Events or General Announcements).
     */
    public Page<AnnouncementResponseDTO> getAnnouncementsByType(Long clubId, Long userId, AnnouncementType type, Pageable pageable) {
        List<AnnouncementResponseDTO> announcements = announcementRepository.findAllAnnouncementsWithSeenStatusAndType(userId, clubId, type);
        return new PageImpl<>(announcements, pageable, announcements.size());
    }

    /**
     * Retrieves paginated unseen announcements by type (Events or General Announcements).
     */
    public Page<AnnouncementResponseDTO> getUnseenAnnouncementsByType(Long clubId, Long userId, AnnouncementType type, Pageable pageable) {
        List<AnnouncementResponseDTO> unseenAnnouncements = announcementRepository.findUnseenAnnouncementsWithSeenStatusAndType(userId, clubId, type);
        return new PageImpl<>(unseenAnnouncements, pageable, unseenAnnouncements.size());
    }


    /**
     * Converts an AnnouncementResponseDTO to AnnouncementDTO.
     */
    private AnnouncementDTO convertToAnnouncementDTO(AnnouncementResponseDTO responseDTO) {
        AnnouncementDTO announcementDTO = new AnnouncementDTO();
        announcementDTO.setPostedById(responseDTO.getId());
        announcementDTO.setTitle(responseDTO.getTitle());
        announcementDTO.setContent(responseDTO.getContent());
        announcementDTO.setType(responseDTO.getType());
        announcementDTO.setAnnouncementImageUrl(responseDTO.getAnnouncementImageUrl());
        return announcementDTO;
    }

}
