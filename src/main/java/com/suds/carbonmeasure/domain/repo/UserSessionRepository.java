package com.suds.carbonmeasure.domain.repo;

import com.suds.carbonmeasure.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    UserSession findBySessionToken(String sessionToken);
    void deleteByUserId(Long userId);
}

