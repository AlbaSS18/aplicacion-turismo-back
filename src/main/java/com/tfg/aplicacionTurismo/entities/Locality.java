package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * Clase que representa la localidad donde se encuentra situada una actividad.
 */
@Entity
public class Locality {

    @Id
    @GeneratedValue
    private long id;
    @NotEmpty
    @Column (unique = true)
    private String nameLocality;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locality")
    private Set<Activity> activityList;

    /**
     * Constructor de la clase Locality
     */
    public Locality() {
    }

    /**
     * Constructor de la clase Locality
     * @param nameLocality nombre de la localidad
     */
    public Locality(@NotEmpty String nameLocality) {
        this.nameLocality = nameLocality;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameLocality() {
        return nameLocality;
    }

    public void setNameLocality(String nameLocality) {
        this.nameLocality = nameLocality;
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
            throw new RuntimeException("Can't remove a locality that has activities");
        }
    }

}
