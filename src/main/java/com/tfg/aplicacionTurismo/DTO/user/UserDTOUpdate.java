package com.tfg.aplicacionTurismo.DTO.user;

import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

public class UserDTOUpdate {

    @NotNull
    private int age;
    @NotEmpty
    private String genre;
    @NotEmpty
    private String userName;
    @NotEmpty
    private Set<String> roles;

    private Optional<Set<InterestByUserDTO>> interest;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Optional<Set<InterestByUserDTO>> getInterest() {
        return interest;
    }

    public void setInterest(Optional<Set<InterestByUserDTO>> interest) {
        this.interest = interest;
    }
}
