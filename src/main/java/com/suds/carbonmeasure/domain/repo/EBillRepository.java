package com.suds.carbonmeasure.domain.repo;
import com.suds.carbonmeasure.domain.ElectricityBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EBillRepository extends JpaRepository<ElectricityBill, Long> {

    @Query(value = "SELECT * FROM electricity_bill WHERE user_id = :userId " +
            "ORDER BY STR_TO_DATE(issue_date, '%d %b %Y')", nativeQuery = true)
    List<ElectricityBill> getAllByUserIdOrderByIssueDate(@Param("userId") long userId);

    ElectricityBill findElectricityBillByAccountNumberAndIssueDateAndUserId(String accountNumber,String issueDate,
                                                                            Long userId);

    void deleteAllByUserId(long userId);
}

