package com.tfg.aplicacionTurismo.DTO.activity;

import javax.validation.constraints.NotBlank;

/**
 * Clase que define los atributos del objeto que se utiliza para transmitir el metadata de una imagen.
 */
public class ImageDTO {
    @NotBlank
    private String fileName;
    @NotBlank
    private String mimeType;
    @NotBlank
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
