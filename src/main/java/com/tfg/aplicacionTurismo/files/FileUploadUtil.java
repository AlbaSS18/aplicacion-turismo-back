package com.tfg.aplicacionTurismo.files;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Clase que se encarga de la gestión de ficheros de imagen.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class FileUploadUtil {

    /**
     * Método que se utiliza para guardar los ficheros de imagen en el sistema de archivos local.
     * @param uploadDir ruta del directorio dónde se almacenarán las imágenes.
     * @param fileName nombre del archivo de imagen.
     * @param multipartFile la imagen.
     * @throws IOException si no se pudo guardar el fichero en el directorio.
     */
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        System.out.println(uploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    /**
     * Método que se utiliza para eliminar el ficheros de imagen del sistema de archivos local.
     * @param uploadDir ruta del archivo.
     */
    public static void removeFile(String uploadDir){
        try {
            boolean result = Files.deleteIfExists(Paths.get(uploadDir));
            if (result) {
                System.out.println("File is deleted!");
            } else {
                System.out.println("Sorry, unable to delete the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
