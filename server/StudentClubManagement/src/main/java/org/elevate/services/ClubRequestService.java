package org.elevate.services;

import org.springframework.stereotype.Service;
import org.elevate.dtos.ClubRequestDTO;
import org.elevate.exceptions.*;
import org.elevate.models.*;
import org.elevate.repositories.ClubRequestRepository;
import org.elevate.repositories.ClubRepository;
import org.elevate.repositories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

        // Ensure user has not already requested to join this club
        if (clubRequestRepository.existsByUserAndClub(user, club)) {
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
    public List<ClubRequest> getClubRequestsByClubId(Long clubId, RequestStatus status) {
        return clubRequestRepository.findByClub_ClubIdAndStatusEquals(clubId, status);
    }

    /**
     * Get all club requests for of a user based on the status of the request(Student View)
     */
    public List<ClubRequest> getClubRequestsByUserId(Long userId, RequestStatus status) {
        return clubRequestRepository.findByUser_UserIdAndStatusEquals(userId, status);
    }

    public ClubRequest patchUpdates(Long requestId, Map<String, String> updates) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        if(!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new RuntimeException("Club request is already processed.");
        }

        AtomicBoolean isStatusUpdate = new AtomicBoolean(false);
        StringBuilder statusUpdate = new StringBuilder();
        StringBuilder approverComment = new StringBuilder();

        updates.forEach((key, value) -> {
            switch (key) {
                case "status" -> {
                    isStatusUpdate.set(true);
                    if(value.equalsIgnoreCase("approved")) statusUpdate.append("approved");
                    else if(value.equalsIgnoreCase("rejected")) statusUpdate.append("rejected");
                    else throw new RuntimeException("Invalid value for status. Available values: Approved and Rejected");
                }
                case "approverComment" -> {
                    if(isStatusUpdate.get() && !value.isBlank()) approverComment.append(value);
                }
                case "userComment" -> {
                    if(!value.isBlank()) clubRequest.setUserComment(value);
                }
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        ClubRequest clubRequestUpdate = new ClubRequest();
        if(isStatusUpdate.get()) {
            if(statusUpdate.toString().equalsIgnoreCase("approved")) clubRequestUpdate =  userClubService.approveClubRequest(requestId, approverComment.toString());
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

        // Ensure request is still pending
        if (!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new RuntimeException("Club request is already processed.");
        }

        clubRequest.setStatus(RequestStatus.REJECTED);
        clubRequest.setApproverComment(approverComment);
        return  clubRequestRepository.save(clubRequest);
    }
}
