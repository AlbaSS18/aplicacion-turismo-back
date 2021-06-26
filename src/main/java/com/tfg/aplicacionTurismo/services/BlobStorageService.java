package com.tfg.aplicacionTurismo.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Servicio que se utiliza para conectar la aplicación con el contenedor de archivos almacenado en el servicio de Azure.
 */
@Service
public class BlobStorageService {

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    private CloudBlobClient blobClient;

    /**
     * Método que se utiliza para subir una imagen al contenedor de Azure.
     * @param filename nombre del archivo.
     * @param content flujo de entrada de bytes del archivo de imagen.
     * @param length tamaño de la imagen.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws InvalidKeyException si la key es inválida.
     */
    public void upload(String filename, InputStream content, long length) throws URISyntaxException, StorageException, IOException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        CloudBlockBlob blob = container.getBlockBlobReference(filename);
        blob.upload(content, length);
    }

    /**
     * Método que se utiliza para obtener un archivo del contenedor de Azure.
     * @param filename nombre del archivo.
     * @return flujo de salida en el que los datos se escriben en una matriz de bytes.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     */
    public ByteArrayOutputStream getFile(String filename) throws URISyntaxException, StorageException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("activities");
        CloudBlockBlob blob = container.getBlockBlobReference(filename);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        blob.download(os);
        return os;
    }

    /**
     * Método que se utiliza para eliminar una imagen del contenedor de Azure.
     * @param filename nombre del archivo.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     * @throws InvalidKeyException si la key es inválida.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     */
    public void deleteFile(String filename) throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("activities");
        CloudBlockBlob blob = container.getBlockBlobReference(filename);

        blob.deleteIfExists();
    }
}
