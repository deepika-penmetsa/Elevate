package org.elevate.services;

import jakarta.transaction.Transactional;
import org.elevate.dtos.ClubRequestForClub_ResponseDTO;
import org.elevate.dtos.ClubRequestForUser_ResponseDTO;
import org.springframework.stereotype.Service;
import org.elevate.exceptions.*;
import org.elevate.models.*;
import org.elevate.repositories.ClubRequestRepository;
import org.elevate.repositories.ClubRepository;
import org.elevate.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ClubRequestService {

    private final ClubRequestRepository clubRequestRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubService userClubService;

    public ClubRequestService(ClubRequestRepository clubRequestRepository, UserRepository userRepository, ClubRepository clubRepository, UserClubService userClubService) {
        this.clubRequestRepository = clubRequestRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.userClubService = userClubService;
    }

    /**
     *  Create a new Club Request
     */
    public ClubRequest createClubRequest(Long userId, Long clubId, String userComment) throws ClubLimitExceededException, RequestAlreadyExistsException, ClubNotFoundException, InvalidRequestException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        // Ensure user has not exceeded club limit (Max 3 clubs)
        if (user.getJoinedClubs() >= 3) {
            throw new ClubLimitExceededException("User has already joined 3 clubs.");
        }

        // Ensure user has not already requested to join this club which is still in PENDING STATUS.
        if (clubRequestRepository.existsByUserAndClubAndStatusEquals(user, club, RequestStatus.PENDING)) {
            throw new RequestAlreadyExistsException("User has already requested to join this club.");
        }
        // Ensure user has not already in the club for which he/she is requesting.
        if(userClubService.existsByUserAndClub(user, club)){
            throw new InvalidRequestException("User has already requested to join this club.");
        }

        ClubRequest clubRequest = new ClubRequest();
        clubRequest.setUser(user);
        clubRequest.setClub(club);
        clubRequest.setUserComment(userComment);
        clubRequest.setStatus(RequestStatus.PENDING);

        return clubRequestRepository.save(clubRequest);
    }

    /**
     * Get all club requests for a club based on the status of a request(Club Admin and SuperAdmin)
     */
    public List<ClubRequestForClub_ResponseDTO> getClubRequestsByClubId(Long clubId, String status) {
        List<ClubRequest> clubRequests = new ArrayList<>();

        if(status.equals("ALL")) clubRequests = clubRequestRepository.findByClubIdAndStatusNotEquals(clubId, RequestStatus.WITHDRAWN);
        else{
            RequestStatus requestStatus = RequestStatus.valueOf(status);
            clubRequests = clubRequestRepository.findByClub_ClubIdAndStatusEquals(clubId, requestStatus);
        }
        return clubRequests.stream()
                .map(this::clubRequestForClub_ResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all club requests for of a user based on the status of the request(Student View)
     */
    public List<ClubRequestForUser_ResponseDTO> getClubRequestsByUserId(Long userId, String status) {
        List<ClubRequest> clubRequests = new ArrayList<>();
        if(status.equals("ALL")) clubRequests = clubRequestRepository.findByUser_UserId(userId);
        else{
            RequestStatus requestStatus = RequestStatus.valueOf(status);
            clubRequests = clubRequestRepository.findByUser_UserIdAndStatusEquals(userId, requestStatus);
        }
        return clubRequests.stream()
                .map(this::clubRequestForUser_ResponseDTO)
                .collect(Collectors.toList());
    }

    private ClubRequestForClub_ResponseDTO clubRequestForClub_ResponseDTO(ClubRequest cr) {
        ClubRequestForClub_ResponseDTO crResponseDTO = new ClubRequestForClub_ResponseDTO();
        crResponseDTO.setClubRequestId(cr.getId());
        /**
         * USER DETAILS
         */
        User user = cr.getUser();
        if (user != null) {
            crResponseDTO.setUserId(user.getUserId());
            crResponseDTO.setUserFirstName(user.getFirstName() != null ? user.getFirstName() : "N/A");
            crResponseDTO.setUserLastName(user.getLastName() != null ? user.getLastName() : "N/A");
            crResponseDTO.setUserEmail(user.getEmail() != null ? user.getEmail() : "N/A");
            crResponseDTO.setUserPhone(user.getPhone() != null ? user.getPhone() : "N/A");
            crResponseDTO.setUserBio(user.getBio() != null ? user.getBio() : "N/A");
            crResponseDTO.setUserApartment(user.getApartment() != null ? user.getApartment() : "N/A");
            crResponseDTO.setUserStreet(user.getStreet() != null ? user.getStreet() : "N/A");
            crResponseDTO.setUserCity(user.getCity() != null ? user.getCity() : "N/A");
            crResponseDTO.setUserState(user.getState() != null ? user.getState() : "N/A");
            crResponseDTO.setUserZipcode(user.getZipcode() != null ? user.getZipcode() : "N/A");
            crResponseDTO.setUserCountry(user.getCountry() != null ? user.getCountry() : "N/A");
            crResponseDTO.setUserJoinedClubs(user.getJoinedClubs());
            crResponseDTO.setUserRole(user.getRole() != null ? user.getRole() : null);

            // Convert profile photo to Base64 string if available
            if (user.getProfilePhoto() != null) {
                crResponseDTO.setUserProfilePhoto(Base64.getEncoder().encodeToString(user.getProfilePhoto()));
            } else {
                crResponseDTO.setUserProfilePhoto(null);
            }
        }

        crResponseDTO.setRequestStatus(cr.getStatus());
        crResponseDTO.setUserComment(cr.getUserComment());
        crResponseDTO.setApproverComment(cr.getApproverComment());
        crResponseDTO.setRequestCreatedAt(cr.getCreatedAt());
        crResponseDTO.setRequestUpdatedAt(cr.getUpdatedAt());

        return crResponseDTO;
    }

    private ClubRequestForUser_ResponseDTO clubRequestForUser_ResponseDTO(ClubRequest cr) {
        ClubRequestForUser_ResponseDTO crResponseDTO = new ClubRequestForUser_ResponseDTO();
        crResponseDTO.setClubRequestId(cr.getId());

        /**
         * CLUB DETAILS
         */
        Club club = cr.getClub();
        if (club != null) {
            crResponseDTO.setClubId(club.getClubId());
            crResponseDTO.setClubName(club.getClubName() != null ? club.getClubName() : "N/A");
            crResponseDTO.setClubDescription(club.getDescription() != null ? club.getDescription() : "N/A");
            crResponseDTO.setClubNoOfMembers(club.getNoOfMembers());
            crResponseDTO.setClubAvailableSlots(club.getAvailableSlots());
            crResponseDTO.setClubTotalSlots(club.getTotalSlots());
            // Convert Club Image to Base64 string if available
            if(club.getClubImage() != null) crResponseDTO.setClubImageUrl(Base64.getEncoder().encodeToString(club.getClubImage()));
            else crResponseDTO.setClubImageUrl(null);
            // Convert Club Background Image to Base64 string if available
            if(club.getClubBackgroundImage() != null) crResponseDTO.setClubBackgroundImageUrl(Base64.getEncoder().encodeToString(club.getClubBackgroundImage()));
            else crResponseDTO.setClubBackgroundImageUrl(null);

        }

        crResponseDTO.setRequestStatus(cr.getStatus());
        crResponseDTO.setUserComment(cr.getUserComment());
        crResponseDTO.setApproverComment(cr.getApproverComment());
        crResponseDTO.setRequestCreatedAt(cr.getCreatedAt());
        crResponseDTO.setRequestUpdatedAt(cr.getUpdatedAt());

        return crResponseDTO;
    }



    @Transactional
    public ClubRequest patchUpdates(Long requestId, Map<String, String> updates) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        if(!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new RuntimeException("Club request is already processed.");
        }

        AtomicBoolean isStatusUpdate = new AtomicBoolean(false);
        StringBuilder statusUpdate = new StringBuilder();
        StringBuilder approverComment = new StringBuilder();

        // First pass: Process "status" first
        updates.forEach((key, value) -> {
            if ("status".equals(key)) {
                isStatusUpdate.set(true);
                if ("approved".equalsIgnoreCase(value)) {
                    statusUpdate.append("approved");
                } else if ("rejected".equalsIgnoreCase(value)) {
                    statusUpdate.append("rejected");
                } else {
                    throw new IllegalArgumentException("Invalid value for status. Available values: Approved and Rejected");
                }
            }
        });

        // Second pass: Process other fields after "status"
        updates.forEach((key, value) -> {
            switch (key) {
                case "status" -> {} // Skip "status" since it's already processed

                case "approverComment" -> {
                    if (isStatusUpdate.get() && value != null && !value.isBlank()) {
                        approverComment.append(value);
                    }
                }
                case "userComment" -> {
                    if (value != null && !value.isBlank()) {
                        clubRequest.setUserComment(value);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });


        ClubRequest clubRequestUpdate = new ClubRequest();
        if(isStatusUpdate.get()) {

            // Ensure request is still pending
            if (!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
                throw new RuntimeException("Club request is already processed.");
            }
            if(statusUpdate.toString().equalsIgnoreCase("approved")){
                try {
                    clubRequestUpdate =  userClubService.approveClubRequest(requestId, approverComment.toString());
                } catch (ClubLimitExceededException e) {
                    throw new ClubLimitExceededException("User have reached the club limit. User cannot join anymore clubs.");
                } catch (ClubCapacityExceededException e) {
                    throw new ClubCapacityExceededException("The club has reached its maximum capacity. Cannot add more members.");
                } catch (RequestAlreadyExistsException e) {
                    throw new RequestAlreadyExistsException("This User already exists in this club. Please check before approving again.");
                }
            }
            else if(statusUpdate.toString().equalsIgnoreCase("rejected")) clubRequestUpdate =  rejectClubRequest(requestId, approverComment.toString());
        }
        return clubRequestUpdate;
    }

    /**
     * Reject a Club Request
     *
     * @return
     */
    public ClubRequest rejectClubRequest(Long requestId, String approverComment) {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        clubRequest.setStatus(RequestStatus.REJECTED);
        clubRequest.setApproverComment(approverComment);
        return  clubRequestRepository.save(clubRequest);
    }

    @Transactional
    public void withdrawClubRequest(Long requestId) throws RecordNotFoundException {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RecordNotFoundException("Club Request with Id: "+requestId+" not found!"));
        clubRequest.setStatus(RequestStatus.WITHDRAWN);
        clubRequestRepository.save(clubRequest);
    }
}
