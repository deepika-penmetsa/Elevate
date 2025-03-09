package org.studentclubmanagement.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.studentclubmanagement.dtos.ClubResponseDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.*;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.UserClubRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserClubRepository userClubRepository;

    public List<ClubResponseDTO> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(this::mapToClubResponseDTO)
                .collect(Collectors.toList());
    }

    public ClubResponseDTO getClubById(Long id) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));
        return mapToClubResponseDTO(club);
    }

    @Transactional
    public ClubResponseDTO createClub(ClubResponseDTO clubResponseDTO) {
        Club club = new Club();
        club.setClubName(clubResponseDTO.getClubName());
        club.setDescription(clubResponseDTO.getDescription());

        // Default values
        club.setTotalSlots(clubResponseDTO.getTotalSlots() > 0 ? clubResponseDTO.getTotalSlots() : 20);
        club.setNoOfMembers(0);
        club.setAvailableSlots(club.getTotalSlots());

        // Assign Club Admin if provided
        if (clubResponseDTO.getAdminId() != null) {
            assignClubAdmin(club, clubResponseDTO.getAdminId());
        }

        saveClubImages(club, clubResponseDTO.getClubImage(), clubResponseDTO.getClubBackgroundImage());

        Club savedClub = clubRepository.save(club);
        return mapToClubResponseDTO(savedClub);
    }

    @Transactional
    public ClubResponseDTO updateClub(Long id, ClubResponseDTO updatedClubResponseDTO, MultipartFile clubImage, MultipartFile clubBackgroundImage) throws ClubNotFoundException {
        Club existingClub = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));

        existingClub.setClubName(updatedClubResponseDTO.getClubName());
        existingClub.setDescription(updatedClubResponseDTO.getDescription());

        // Handle Total Slots, Available Slots, and Club Admin
        if(updatedClubResponseDTO.getTotalSlots() > 0) updateSlots(existingClub, updatedClubResponseDTO.getTotalSlots());

        if (updatedClubResponseDTO.getAdminId() != null) {
            assignClubAdmin(existingClub, updatedClubResponseDTO.getAdminId());
        }

        saveClubImages(existingClub, clubImage, clubBackgroundImage);

        Club updatedClub = clubRepository.save(existingClub);
        return mapToClubResponseDTO(updatedClub);
    }

    @Transactional
    public ClubResponseDTO patchClub(Long id, Map<String, String> updates, MultipartFile clubImage, MultipartFile clubBackgroundImage) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "clubName" -> {
                    if (!value.isBlank()) club.setClubName(value);
                }
                case "description" -> {
                    if (!value.isBlank()) club.setDescription(value);
                }
                case "availableSlots" -> {
                    if (!value.isBlank()) {
                        int availableSlots = Integer.parseInt(value);
                        if (availableSlots > club.getTotalSlots()) {
                            throw new IllegalArgumentException("Available slots cannot be greater than total slots.");
                        }
                        club.setAvailableSlots(availableSlots);
                    }
                }
                case "totalSlots" -> {
                    if (!value.isBlank()) {
                        updateSlots(club, Integer.parseInt(value));
                    }
                }
                case "adminId" -> {
                    if (!value.isBlank()) {
                        assignClubAdmin(club, Long.parseLong(value));
                    }
                }
            }
        });

        saveClubImages(club, clubImage, clubBackgroundImage);

        return mapToClubResponseDTO(clubRepository.save(club));
    }

    @Transactional
    public ClubResponseDTO patchClubImage(Long id, MultipartFile clubImage) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        try {
            if (clubImage == null || clubImage.isEmpty()) club.setClubImage(null);
            else club.setClubImage(clubImage.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        return mapToClubResponseDTO(clubRepository.save(club));
    }

    @Transactional
    public ClubResponseDTO patchClubBackgroundImage(Long id, MultipartFile clubBackgroundImage) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        try {
            if (clubBackgroundImage == null || clubBackgroundImage.isEmpty()) club.setClubBackgroundImage(null);
            else  club.setClubBackgroundImage(clubBackgroundImage.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        return mapToClubResponseDTO(clubRepository.save(club));
    }

    public void deleteClub(Long id) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));
        clubRepository.delete(club);
    }

    @Transactional
    protected void assignClubAdmin(Club club, Long adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if user is already a Club Admin
        if (user.getRole() == Role.CLUB_ADMIN) {
            throw new IllegalArgumentException("User is already a Club Admin.");
        }
        // Check if user is a Super Admin
        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("User is a Super Admin and Cannot be assigned to Club Admin.");
        }

        // Check if user is in less than 3 clubs
        if (user.getJoinedClubs() >= 3) {
            throw new IllegalArgumentException("User is already in 3 clubs and cannot join another.");
        }

        // Assign user as Club Admin
        user.setRole(Role.CLUB_ADMIN);
        user.setJoinedClubs(user.getJoinedClubs() + 1);
        club.setClubAdmin(user);

        // Adjust club slots
        club.setNoOfMembers(club.getNoOfMembers() + 1);
        club.setAvailableSlots(club.getAvailableSlots() - 1);

        // Add user to UserClub table
        UserClub userClub = new UserClub();
        userClub.setUser(user);
        userClub.setClub(club);
        userClubRepository.save(userClub);

        userRepository.save(user);
    }

    private void updateSlots(Club club, int newTotalSlots) {
        if (newTotalSlots < club.getNoOfMembers()) {
            throw new IllegalArgumentException("Total slots cannot be less than the number of current members.");
        }

        int usedSlots = club.getTotalSlots() - club.getAvailableSlots();
        int newAvailableSlots = newTotalSlots - usedSlots;
        club.setTotalSlots(newTotalSlots);
        club.setAvailableSlots(newAvailableSlots);
    }

    private void saveClubImages(Club club, MultipartFile clubImage, MultipartFile clubBackgroundImage) {
        try {
            if (clubImage != null && !clubImage.isEmpty()) {
                club.setClubImage(clubImage.getBytes());
            }
            if (clubBackgroundImage != null && !clubBackgroundImage.isEmpty()) {
                club.setClubBackgroundImage(clubBackgroundImage.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private ClubResponseDTO mapToClubResponseDTO(Club club) {
        ClubResponseDTO clubResponseDTO = new ClubResponseDTO();
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
}
