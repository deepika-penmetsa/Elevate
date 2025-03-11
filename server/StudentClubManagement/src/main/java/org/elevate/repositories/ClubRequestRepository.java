package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.elevate.models.*;

import java.util.List;

@Repository
public interface ClubRequestRepository extends JpaRepository<ClubRequest, Long> {

    boolean existsByUserAndClub(User user, Club club);
//    List<ClubRequest> findByUser_UserId(Long userId);
    List<ClubRequest> findByClub_ClubIdAndStatusEquals(Long clubId, RequestStatus status);
    List<ClubRequest> findByUser_UserIdAndStatusEquals(Long userId, RequestStatus status);
}