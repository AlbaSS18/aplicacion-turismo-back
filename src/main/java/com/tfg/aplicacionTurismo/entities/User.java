package com.tfg.aplicacionTurismo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id @GeneratedValue private Long id;

    @NotNull
    @Column (unique = true) private String email;

    @NotNull private int age;
    @NotNull private String genre;

    @NotNull private String userName;

    @NotNull private String password;

    @JoinTable(
            name = "rel_user_rol",
            joinColumns = @JoinColumn(name = "FK_USER", nullable = false),
            inverseJoinColumns = @JoinColumn(name="FK_ROL", nullable = false)
    )

    @NotNull
    @ManyToMany
    private Set<Rol> role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotNull
    Set<RelUserInterest> priority = new HashSet<>();

    /*@JoinTable(
            name = "rel_user_interest",
            joinColumns = @JoinColumn(name = "FK_USER", nullable = false),
            inverseJoinColumns = @JoinColumn(name="FK_INTEREST", nullable = false)
    )

    @NotNull @ManyToMany(cascade = CascadeType.ALL) private Set<Interest> interest;*/

    @OneToMany(mappedBy = "user")
    private Set<RelUserActivity> relUserActivity = new HashSet<>();

    public User (){
    }

    public User(@NotNull String email, @NotNull int age, @NotNull String genre, @NotNull String userName, @NotNull String password) {
        this.email = email;
        this.age = age;
        this.genre = genre;
        this.userName = userName;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String nameUser) {
        this.userName = nameUser;
    }

    public Set<Rol> getRole() {
        return role;
    }

    public void setRole(Set<Rol> role) {
        this.role = role;
    }

    public Set<RelUserInterest> getPriority() {
        return priority;
    }

    public void setPriority(Set<RelUserInterest> priority) {
        this.priority = priority;
    }

    public Set<RelUserActivity> getRelUserActivity() {
        return relUserActivity;
    }

    public void setRelUserActivity(Set<RelUserActivity> relUserActivity) {
        this.relUserActivity = relUserActivity;
    }
}
