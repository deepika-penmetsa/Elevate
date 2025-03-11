package org.elevate.services;

import org.elevate.dtos.ClubResponseDTO;
import org.elevate.dtos.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.elevate.exceptions.*;
import org.elevate.models.*;
import org.elevate.repositories.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserClubService {

    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubRequestRepository clubRequestRepository;

    public UserClubService(UserClubRepository userClubRepository, UserRepository userRepository,
                           ClubRepository clubRepository, ClubRequestRepository clubRequestRepository) {
        this.userClubRepository = userClubRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.clubRequestRepository = clubRequestRepository;
    }

    /**
     * Approve a Club Request and Add User to UserClub
     *
     * @return
     */
    @Transactional
    public ClubRequest approveClubRequest(Long requestId, String approverComment) throws ClubCapacityExceededException, ClubLimitExceededException, RequestAlreadyExistsException {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        User user = clubRequest.getUser();
        Club club = clubRequest.getClub();

        // Check constraints before approving
        if (user.getJoinedClubs() >= 3) {
            throw new ClubLimitExceededException("User has already joined 3 clubs.");
        }
        if (club.getAvailableSlots() <= 0) {
            throw new ClubCapacityExceededException("Club is full, no available slots.");
        }
        if (userClubRepository.existsByUser_UserIdAndClub_ClubId(user.getUserId(), club.getClubId())) {
            throw new RequestAlreadyExistsException("User is already a member of this club.");
        }

        // Create new UserClub entry
        UserClub userClub = new UserClub();
        userClub.setUser(user);
        userClub.setClub(club);
        userClubRepository.save(userClub);

        // Update User & Club Details
        user.setJoinedClubs(user.getJoinedClubs() + 1);
        club.setNoOfMembers(club.getNoOfMembers() + 1);

        userRepository.save(user);
        clubRepository.save(club);

        // Update Request Status
        clubRequest.setStatus(RequestStatus.APPROVED);
        clubRequest.setApproverComment(approverComment);
        return clubRequestRepository.save(clubRequest);
    }


    /**
     * Get All Clubs a User Has Joined
     */
    public List<ClubResponseDTO> getUserClubs(Long userId) {
        return userClubRepository.findByUser_UserId(userId)
                .stream()
                .map(this::mapToClubResponseDTO)
                .collect(Collectors.toList());
    }

    private ClubResponseDTO mapToClubResponseDTO(UserClub userClub) {
        ClubResponseDTO clubResponseDTO = new ClubResponseDTO();
        Club club = userClub.getClub();
        clubResponseDTO.setClubId(club.getClubId());
        clubResponseDTO.setClubName(club.getClubName());
        clubResponseDTO.setDescription(club.getDescription());
        clubResponseDTO.setNoOfMembers(club.getNoOfMembers());
        clubResponseDTO.setAvailableSlots(club.getAvailableSlots());
        clubResponseDTO.setTotalSlots(club.getTotalSlots());

        if (club.getClubAdmin() != null) {
            clubResponseDTO.setAdminId(club.getClubAdmin().getUserId());
        }

        if (club.getClubImage() != null) {
            clubResponseDTO.setClubImageUrl(Base64.getEncoder().encodeToString(club.getClubImage()));
        }

        if (club.getClubBackgroundImage() != null) {
            clubResponseDTO.setClubBackgroundImageUrl(Base64.getEncoder().encodeToString(club.getClubBackgroundImage()));
        }

        return clubResponseDTO;
    }

    /**
     * GET ALL USERS of a CLUB
     */
    public List<UserResponseDTO> getUsersOfAClub(Long clubId) {
        return userClubRepository.findByClub_ClubId(clubId)
                .stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList()); 
    }

    private UserResponseDTO mapToUserResponseDTO(UserClub userClub) {
        UserResponseDTO dto = new UserResponseDTO();
        User user = userClub.getUser();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStreet(user.getStreet());
        dto.setApartment(user.getApartment());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZipcode(user.getZipcode());
        dto.setCountry(user.getCountry());

        // Convert profile photo to Base64 string
        if (user.getProfilePhoto() != null) {
            dto.setProfilePhoto(Base64.getEncoder().encodeToString(user.getProfilePhoto()));
        } else {
            dto.setProfilePhoto(null);
        }

        dto.setBio(user.getBio());
        dto.setJoinedClubs(user.getJoinedClubs());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    public boolean existsByUserAndClub(User user, Club club) {
        return userClubRepository.existsByUser_UserIdAndClub_ClubId(user.getUserId(), club.getClubId());
    }
}
