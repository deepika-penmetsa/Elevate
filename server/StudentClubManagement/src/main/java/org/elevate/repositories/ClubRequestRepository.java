package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.elevate.models.*;

import java.util.List;

@Repository
public interface ClubRequestRepository extends JpaRepository<ClubRequest, Long> {

    List<ClubRequest> findByUser_UserId(Long userId);
    List<ClubRequest> findByUser_UserIdAndStatusEquals(Long userId, RequestStatus status);
    boolean existsByUserAndClubAndStatusEquals(User user, Club club, RequestStatus pending);

    @Query("""
        SELECT cr 
        FROM ClubRequest cr 
        WHERE cr.club.clubId = :clubId AND cr.status != :status
    """)
    List<ClubRequest> findByClubIdAndStatusNotEquals(@Param("clubId") Long clubId, @Param("status") RequestStatus status);
    List<ClubRequest> findByClub_ClubIdAndStatusEquals(Long clubId, RequestStatus requestStatus);
}