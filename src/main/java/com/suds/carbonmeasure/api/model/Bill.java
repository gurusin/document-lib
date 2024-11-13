package com.suds.carbonmeasure.api.model;

import lombok.Data;

@Data
public class Bill {

    private long id;
    private int userId;
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
}
