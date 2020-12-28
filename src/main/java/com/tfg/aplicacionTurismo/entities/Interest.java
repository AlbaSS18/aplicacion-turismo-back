package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Interest {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Column(unique = true)
    private String nameInterest;

    /*@ManyToMany(mappedBy = "interest")
    private Set<User> user;*/

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL)
    @NotNull
    Set<RelUserInterest> priority = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "interest")
    private Set<Activity> activities;

    public Interest() {
    }

    public Interest(String nameInterest) {
        this.nameInterest = nameInterest;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String name) {
        this.nameInterest = name;
    }

    public Set<RelUserInterest> getPriority() {
        return priority;
    }

    public void setPriority(Set<RelUserInterest> priority) {
        this.priority = priority;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }
}
