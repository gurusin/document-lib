package com.suds.carbonmeasure.service;

import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlobInputStream;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.stereotype.Component;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class AzureStorageService {

    private final BlobContainerClient blobContainerClient;

    public AzureStorageService(
            @Value("${azure.storage.account-name}") String accountName,
            @Value("${azure.storage.account-key}") String accountKey,
            @Value("${azure.storage.container-name}") String containerName) {

        String endpoint = String.format("https://%s.blob.core.windows.net", accountName);
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
//        this.blobContainerClient = new BlobServiceClientBuilder()
//                .endpoint(endpoint)
//                .credential(new DefaultAzureCredentialBuilder().
//                        build())
//                .buildClient()
//                .getBlobContainerClient(containerName);
        this.blobContainerClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(credential)  // Use the shared key credential
                .buildClient()
                .getBlobContainerClient(containerName);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique filename using UUID
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        // Get a BlobClient for the specific file name
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        // Upload file to the blob
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        // Return the file URL
        return fileName;
    }

    public BlobInputStream getFile(String blobName) {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        return blobClient.openInputStream();

    }

    public void deleteFile(String docName) {
        BlobClient blobClient = blobContainerClient.getBlobClient(docName);
        blobClient.delete();
    }
}
