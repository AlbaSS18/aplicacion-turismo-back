package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

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

    public City() {
    }

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
}
