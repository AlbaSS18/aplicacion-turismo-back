package com.tfg.aplicacionTurismo.DTO.activity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para recomendar actividades a un usuario.
 */
public class ActivityRecommendationDTO implements Comparable<ActivityRecommendationDTO> {

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
    private String city;
    @NotBlank
    private String interest;
    @NotBlank
    private String address;
    @NotNull @Valid
    private ImageDTO metadataImage;
    @NotNull
    private double score;
    @NotNull
    private double average;

    /**
     * Constructor de la clase ActivityRecommendationDTO
     */
    public ActivityRecommendationDTO() {
    }

    /**
     * Constructor de la clase ActivityRecommendationDTO
     * @param id identificador de la actividad
     * @param name nombre de la actividad
     * @param description descripción de la actividad
     * @param longitude longitud de la actividad
     * @param latitude latitud de la actividad
     * @param pathImage ruta donde se encuentra la imagen de la actividad
     * @param city localidad donde se encuentra situada la actividad
     * @param interest tipo de interés asociado a la actividad
     * @param address dirección donde se encuentra localizada la actividad
     * @param metadataImage DTO que representa los metadatos de la imagen
     * @param score peso de la actividad en la recomendación
     * @param average media de las valoraciones realizadas a la actividad
     */
    public ActivityRecommendationDTO(@NotNull Long id, @NotBlank String name, @NotBlank String description,
                                     @NotNull double longitude, @NotNull double latitude, @NotBlank String pathImage,
                                     @NotBlank String city, @NotBlank String interest, @NotBlank String address,
                                     @NotNull @Valid ImageDTO metadataImage, @NotNull double score, @NotNull double average) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pathImage = pathImage;
        this.city = city;
        this.interest = interest;
        this.address = address;
        this.metadataImage = metadataImage;
        this.score = score;
        this.average = average;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    @Override
    public int compareTo(ActivityRecommendationDTO o) {
        if(this.score > o.score)
            return -1;
        if(this.score < o.score)
            return 1;
        return 0;
    }
}
