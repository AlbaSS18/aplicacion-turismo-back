package com.tfg.aplicacionTurismo.entities;

import org.springframework.data.geo.Point;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private long id;
    @NotEmpty @Column(unique = true)
    private String name;
    @NotEmpty @Column
    private String description;
    @NotNull @Column
    private Point coordenates;
    @Column
    private String pathImage;

    @NotNull @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private Set<RelUserActivity> relUserActivity= new HashSet<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "interest_id")
    private Interest interest;

    public Activity() {
    }

    public Activity(@NotEmpty String name, @NotEmpty String description, @NotNull Point coordenates, String pathImage) {
        this.name = name;
        this.description = description;
        this.coordenates = coordenates;
        this.pathImage = pathImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Point getCoordenates() {
        return coordenates;
    }

    public void setCoordenates(Point coordenates) {
        this.coordenates = coordenates;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Set<RelUserActivity> getRelUserActivity() {
        return relUserActivity;
    }

    public void setRelUserActivity(Set<RelUserActivity> relUserActivity) {
        this.relUserActivity = relUserActivity;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }
}
