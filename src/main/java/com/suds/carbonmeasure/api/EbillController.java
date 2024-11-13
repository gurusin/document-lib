package com.suds.carbonmeasure.api;

import com.suds.carbonmeasure.api.model.Bill;
import com.suds.carbonmeasure.domain.ElectricityBill;
import com.suds.carbonmeasure.service.ElectricityBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class EbillController {

    @Autowired
    private ElectricityBillService service;

    @GetMapping("/api/consumption")
    public @ResponseBody ResponseEntity<List<ElectricityBill>> getConsumptionData() {
        List<ElectricityBill> bills = service.findForUser();
        return ResponseEntity.ok(bills);
    }

    @PostMapping("/api/consumption/clear")
    public ResponseEntity deleteForUser(){
        service.deleteForUser();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/pdf/update-bill")
    public @ResponseBody ResponseEntity updateBill(@RequestBody Bill bill){
        ElectricityBill b2 = service.updateBill(bill);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("api/document")
    public @ResponseBody ResponseEntity updateBill(@RequestParam("id") long id){
        service.delete(id);
        return ResponseEntity.ok(true);
    }

}
