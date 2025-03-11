package org.elevate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.elevate.models.*;

@Repository
public interface ClubAdminRequestRepository extends JpaRepository<ClubAdminRequest, Long> {
    // Custom query methods if needed
}

