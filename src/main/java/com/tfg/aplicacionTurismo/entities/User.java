package com.tfg.aplicacionTurismo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id @GeneratedValue private long id;

    @NotNull
    @Column (unique = true)
    @Email
    private String email;

    @NotNull
    private Date dateBirthday;

    @NotNull private String userName;

    @NotNull @Size(min=7) private String password;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<RelUserActivity> relUserActivity = new HashSet<>();

    public User (){
    }

    public User(@NotNull @Email String email, @NotNull Date dateBirthday, @NotNull String userName, @NotNull @Size(min = 7) String password) {
        this.email = email;
        this.dateBirthday = dateBirthday;
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

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
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
