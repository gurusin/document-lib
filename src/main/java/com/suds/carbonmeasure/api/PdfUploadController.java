package com.suds.carbonmeasure.api;
import com.azure.storage.blob.specialized.BlobInputStream;
import com.suds.carbonmeasure.domain.ElectricityBill;
import com.suds.carbonmeasure.service.AzureStorageService;
import com.suds.carbonmeasure.service.PDFExtractionService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfUploadController {

    @Autowired
    private PDFExtractionService pdfExtractionService;

    @Autowired
    private AzureStorageService azureStorageService;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam String blobName) {
        try {
            // Get a reference to the container
            BlobInputStream inputStream = azureStorageService.getFile(blobName);

            if (inputStream == null) {
                return ResponseEntity.notFound().build();
            }

            // Return the file as a downloadable response
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)  // Content-Type set to PDF
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blobName + "\"")  // Attachment header
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ElectricityBill> uploadPdf(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("DocType") String docType)  throws Exception{
        if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            // Use PDFTextStripper to extract text from the PDF
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String extractedText = pdfStripper.getText(document);
            ElectricityBill ebill = pdfExtractionService.extractFromPDFAndSave(extractedText,file,docType);
            // Process the extracted text as needed
            // Here we return it directly in the response
            return ResponseEntity.ok(ebill);

        } catch (Exception e) {
           throw e;
        }
    }
}
