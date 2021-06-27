package com.tfg.aplicacionTurismo.DTO.activity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para crear una nueva actividad.
 */
public class ActivityDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotBlank
    private String locality;
    @NotBlank
    private String interest;
    @NotBlank
    private String address;

    /**
     * Constructor de la clase ActivityDTO
     */
    public ActivityDTO() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
