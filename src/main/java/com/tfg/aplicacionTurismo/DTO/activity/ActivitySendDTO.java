package com.tfg.aplicacionTurismo.DTO.activity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para obtener actividad/es.
 */
public class ActivitySendDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotBlank
    private String pathImage;
    @NotBlank
    private String locality;
    @NotBlank
    private String interest;
    @NotBlank
    private String address;
    @NotNull @Valid
    private ImageDTO metadataImage;

    /**
     * Constructor de la clase ActivitySendDTO
     */
    public ActivitySendDTO() {
    }

    /**
     * Constructor de la clase ActivitySendDTO
     * @param id identificador de la actividad
     * @param name nombre de la actividad
     * @param description descripción de la actividad
     * @param longitude longitud de la actividad
     * @param latitude latitud de la actividad
     * @param pathImage ruta donde se encuentra la imagen de la actividad
     * @param locality localidad donde se encuentra situada la actividad
     * @param interest tipo de interés asociado a la actividad
     * @param address dirección donde se encuentra localizada la actividad
     * @param metadataImage DTO que representa los metadatos de la imagen
     */
    public ActivitySendDTO(@NotNull Long id, @NotBlank String name, @NotBlank String description,
                           @NotNull double longitude, @NotNull double latitude, @NotBlank String pathImage,
                           @NotBlank String locality, @NotBlank String interest, @NotBlank String address, @NotNull @Valid ImageDTO metadataImage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pathImage = pathImage;
        this.locality = locality;
        this.interest = interest;
        this.address = address;
        this.metadataImage = metadataImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ImageDTO getMetadataImage() {
        return metadataImage;
    }

    public void setMetadataImage(ImageDTO metadataImage) {
        this.metadataImage = metadataImage;
    }
}
