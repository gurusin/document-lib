package com.suds.carbonmeasure.service;

import com.suds.carbonmeasure.domain.ElectricityBill;
import com.suds.carbonmeasure.domain.repo.EBillRepository;
import com.suds.carbonmeasure.util.pdf.TextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PDFExtractionService {

    @Autowired
    private EBillRepository eBillRepository;

    @Autowired
    private AzureStorageService azureStorageService;

    public ElectricityBill extractFromPDFAndSave(String extractedText, MultipartFile file,
                                                 String docType) throws Exception{
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userId = (String)authentication.getPrincipal();
        TextExtractor extractor = new TextExtractor(extractedText);
        ElectricityBill bill = extractor.extract();
        bill.setUserId(Long.parseLong(userId));
        // Check in the DB
        ElectricityBill db = eBillRepository.findElectricityBillByAccountNumberAndIssueDateAndUserId(bill.getAccountNumber(),
                bill.getIssueDate(),Long.parseLong(userId));
        if(db != null){
            throw new RuntimeException("Bill Already uploaded");
        }
        String fileLocation = azureStorageService.uploadFile(file);
        bill.setFileLocation(fileLocation);
        bill.setDocumentType(docType);
        return eBillRepository.save(bill);
    }

    public static void main(String[] args) {
        File file = new File("files/Energy Bill 1.pdf");
        PDFExtractionService pdfExtractionService = new PDFExtractionService();
        try (PDDocument document = PDDocument.load(file)) {
            // Use PDFTextStripper to extract text from the PDF
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String extractedText = pdfStripper.getText(document);
            //pdfExtractionService.extractFromPDFAndSave(extractedText,file);
            // Process the extracted text as needed
            // Here we return it directly in the response

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
