package org.studentclubmanagement.services;

import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.ClubRequestDTO;
import org.studentclubmanagement.exceptions.*;
import org.studentclubmanagement.models.*;
import org.studentclubmanagement.repositories.ClubRequestRepository;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.UserRepository;

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
    public ClubRequest createClubRequest(ClubRequestDTO clubRequestDTO) throws ClubLimitExceededException, RequestAlreadyExistsException, ClubNotFoundException {
        User user = userRepository.findById(clubRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(clubRequestDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        // Ensure user has not exceeded club limit (Max 3 clubs)
        if (user.getJoinedClubs() >= 3) {
            throw new ClubLimitExceededException("User has already joined 3 clubs.");
        }

        // Ensure user has not already requested to join this club
        if (clubRequestRepository.existsByUserAndClub(user, club)) {
            throw new RequestAlreadyExistsException("User has already requested to join this club.");
        }

        ClubRequest clubRequest = new ClubRequest();
        clubRequest.setUser(user);
        clubRequest.setClub(club);
        clubRequest.setUserComment(clubRequestDTO.getUserComment());
        clubRequest.setStatus(RequestStatus.PENDING);

        return clubRequestRepository.save(clubRequest);
    }

    /**
     * Get all pending club requests for a club (Admin view)
     */
    public List<ClubRequest> getClubRequestsByClubId(Long clubId) {
        return clubRequestRepository.findByClub_ClubIdAndStatus(clubId, RequestStatus.PENDING);
    }

//    /**
//     * Approve a Club Request
//     */
//    public void approveClubRequest(Long requestId, String approverComment) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
//        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Club request not found"));
//
//        // Ensure request is still pending
//        if (!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
//            throw new RuntimeException("Club request is already processed.");
//        }
//
//        // Delegate full approval process to UserClubService
//        userClubService.approveClubRequest(requestId, approverComment);
//    }



    public List<ClubRequest> getClubRequestsByUserId(Long userId) {
        return clubRequestRepository.findByUser_UserId(userId);
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
                    if(value.equals("approved")) statusUpdate.append("approved");
                    else if(value.equals("rejected")) statusUpdate.append("rejected");
                    else throw new RuntimeException("Invalid status value");
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
