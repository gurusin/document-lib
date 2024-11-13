package com.suds.carbonmeasure.domain.repo;
import com.suds.carbonmeasure.domain.CarbonUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarbonUserRepository extends JpaRepository<CarbonUser, Long> {
    CarbonUser findByUsername(String username);
}

