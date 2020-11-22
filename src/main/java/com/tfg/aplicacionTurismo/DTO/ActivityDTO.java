package com.tfg.aplicacionTurismo.DTO;

import javax.validation.constraints.NotEmpty;

public class ActivityDTO {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private double latitude;
    @NotEmpty
    private double longitude;
    private String pathImage;
    @NotEmpty
    private String city;
    @NotEmpty
    private String interest;

    public ActivityDTO() {
    }

    public ActivityDTO(@NotEmpty String name, @NotEmpty String description, @NotEmpty double latitude, @NotEmpty double longitude, String pathImage, @NotEmpty String city, @NotEmpty String interest) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pathImage = pathImage;
        this.city = city;
        this.interest = interest;
    }

    public ActivityDTO(@NotEmpty String description, @NotEmpty double latitude, @NotEmpty double longitude, String pathImage, @NotEmpty String city, @NotEmpty String interest) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pathImage = pathImage;
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

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
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
}
