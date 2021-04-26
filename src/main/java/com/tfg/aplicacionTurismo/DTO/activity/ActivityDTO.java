package com.tfg.aplicacionTurismo.DTO.activity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private String city;
    @NotBlank
    private String interest;
    @NotBlank
    private String address;

    public ActivityDTO() {
    }

    public ActivityDTO(@NotBlank String name, @NotBlank String description, @NotNull double latitude, @NotNull double longitude, @NotBlank String city, @NotBlank String interest) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.interest = interest;
    }

    public ActivityDTO(@NotBlank String description, @NotNull double latitude, @NotNull double longitude, @NotBlank String city, @NotBlank String interest) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.interest = interest;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
