package com.suds.carbonmeasure.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ElectricityBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private String accountNumber;

    private String serviceAddress;

    private String issueDate;

    private String billingPeriod;

    private String nmi;

    private long consumption;

    private double totalCost;

    private double carbonFootPrint;

    private double costForKW;

    private String fileLocation;

    private String documentType;

    public void calculateCostPerUnit() {
        double temp = totalCost/consumption;
        costForKW = Math.round(temp * 1000);
    }
}
