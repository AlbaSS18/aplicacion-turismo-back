package com.tfg.aplicacionTurismo.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Service
public class BlobStorageService {

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    private CloudBlobClient blobClient;

    public void upload(String filename, InputStream content, long length) throws URISyntaxException, StorageException, IOException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        CloudBlockBlob blob = container.getBlockBlobReference(filename);
        blob.upload(content, length);
    }

    public ByteArrayOutputStream getFile(String filename) throws URISyntaxException, StorageException, IOException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("activities");
        CloudBlockBlob blob = container.getBlockBlobReference(filename);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        blob.download(os);
        return os;
    }

    public void deleteFile(String filename) throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("activities");
        CloudBlockBlob blob = container.getBlockBlobReference(filename);

        blob.deleteIfExists();
    }
}
