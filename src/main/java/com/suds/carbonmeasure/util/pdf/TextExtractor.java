package com.suds.carbonmeasure.util.pdf;

import com.suds.carbonmeasure.domain.ElectricityBill;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextExtractor {

    private String extractedText;

    private static final double NSW_FACTOR = 0.73;
    private static final double VIC_FACTOR = 0.85;
    private static final double QL_FACTOR = 0.73;

    public TextExtractor(String extractedText){
        this.extractedText = extractedText;
    }

    private String extractValueWithRegex(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.extractedText);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private long extractConsumption(){
        Pattern pattern = Pattern.compile("\\b([\\d,]+\\.\\d+|[\\d,]+)\\s+kWh\\s");
        Matcher matcher = pattern.matcher(extractedText);

        List<Double> kwhValues = new ArrayList<>();

        // Find and print each kWh value
        while (matcher.find()) {
            String kwhString = matcher.group(1);
            kwhValues.add(Double.parseDouble(kwhString.replaceAll(",","")));
        }
       return Math.round(kwhValues.stream().mapToDouble(Double::doubleValue).sum());
    }

    public  String extractFromToDates() {
        // Regex pattern for dates in "dd MMM yyyy" format
        String regex = "Billing period:\\s*(\\d{2} \\w{3} \\d{4}) to (\\d{2} \\w{3} \\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(extractedText);

        // Find and return the billing period if present
        if (matcher.find()) {
            String startDate = matcher.group(1);
            String endDate = matcher.group(2);
            return startDate + " to " + endDate;
        } else {
            return "Billing period not found";
        }
    }

    public ElectricityBill extract() {
        ElectricityBill bill = new ElectricityBill();
        try {
            bill.setServiceAddress(extractValueWithRegex("Service Address: (.*)"));
            bill.setAccountNumber(extractValueWithRegex("Account number: (.*)"));
            bill.setIssueDate(extractValueWithRegex("Bill issue date: (.*)"));
            bill.setBillingPeriod(extractFromToDates());
            bill.setNmi( extractValueWithRegex("National Metering Identifier \\(NMI\\):(.*)"));
            bill.setConsumption(extractConsumption());
            bill.setTotalCost(extractBillAmount());
            calculateEmission(bill);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bill;
    }

    private void calculateEmission(ElectricityBill bill){
        String address = bill.getServiceAddress();
        double carbonFootPrint =0;
        if(address.contains("NSW ")){
            carbonFootPrint = bill.getConsumption() * NSW_FACTOR;
        }else if(address.contains("VIC ")){
            carbonFootPrint = bill.getConsumption() * VIC_FACTOR;
        }else{
            carbonFootPrint = bill.getConsumption() * QL_FACTOR;
        }
        carbonFootPrint = Math.round(carbonFootPrint);
        bill.setCarbonFootPrint(carbonFootPrint);
        bill.calculateCostPerUnit();
    }

    private double extractBillAmount(){
        String regex = "Total current charges \\(incl\\. GST of \\$.+\\) \\$(\\d{1,3}(?:,\\d{3})*(?:\\.\\d{2})?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(extractedText);
        double totalPrice = 0;
        if (matcher.find()) {
            String chargeAmount = matcher.group(1);
            totalPrice = Double.parseDouble(chargeAmount.replaceAll(",",""));
        } else {
            System.out.println("Total charges not found in the text.");
        }
        return totalPrice;
    }

}

