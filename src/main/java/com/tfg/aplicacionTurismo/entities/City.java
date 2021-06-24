package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * Clase que representa la localidad donde se encuentra situada una actividad.
 */
@Entity
public class City {

    @Id
    @GeneratedValue
    private long id;
    @NotEmpty
    @Column (unique = true)
    private String nameCity;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "city")
    private Set<Activity> activityList;

    /**
     * Constructor de la clase City
     */
    public City() {
    }

    /**
     * Constructor de la clase City
     * @param nameCity nombre de la localidad
     */
    public City(@NotEmpty String nameCity) {
        this.nameCity = nameCity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String name_city) {
        this.nameCity = name_city;
    }

    public Set<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(Set<Activity> activityList) {
        this.activityList = activityList;
    }

    /**
     * Método que comprueba si hay actividades asociadas a la localidad antes de eliminarla
     */
    @PreRemove
    public void checkActivityBeforeRemove() {
        if(!this.activityList.isEmpty()){
            throw new RuntimeException("Can't remove a city that has activities");
        }
    }

}
