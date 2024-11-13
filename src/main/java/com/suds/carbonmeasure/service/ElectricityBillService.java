package com.suds.carbonmeasure.service;

import com.suds.carbonmeasure.api.model.Bill;
import com.suds.carbonmeasure.domain.ElectricityBill;
import com.suds.carbonmeasure.domain.repo.CarbonUserRepository;
import com.suds.carbonmeasure.domain.repo.EBillRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectricityBillService {

    @Autowired
    private EBillRepository eBillRepository;
    @Autowired
    private AzureStorageService azureStorageService;



    public List<ElectricityBill> findForUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String)authentication.getPrincipal();
        return eBillRepository.getAllByUserIdOrderByIssueDate(Long.parseLong(userId));
    }

    @Transactional
    public void deleteForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String)authentication.getPrincipal();
        if(userId != null){
                eBillRepository.deleteAllByUserId(Long.parseLong(userId));
        }
    }

    public ElectricityBill updateBill(Bill bill) {
        ElectricityBill db = eBillRepository.getReferenceById(bill.getId());
        if(db != null){
            db.setBillingPeriod(bill.getBillingPeriod());
            db.setIssueDate(bill.getIssueDate());
            db.setNmi(bill.getNmi());
            db.setConsumption(bill.getConsumption());
            db.setAccountNumber(bill.getAccountNumber());
            db.setTotalCost(db.getTotalCost());
            db.calculateCostPerUnit();
            eBillRepository.save(db);
        }
        return db;
    }

    public void delete(long id) {
        ElectricityBill db = eBillRepository.getReferenceById(id);
        String docName = db.getFileLocation();
        eBillRepository.deleteById(id);
        azureStorageService.deleteFile(docName);
    }
}
