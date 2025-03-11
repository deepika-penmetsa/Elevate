package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.elevate.models.*;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByClubName(String clubName);
}