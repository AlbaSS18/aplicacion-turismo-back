package com.tfg.aplicacionTurismo.DTO.user;

import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;
import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public class UserDTO {

    @NotNull
    private Long id;
    @NotNull
    private int age;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String genre;
    @NotEmpty
    private String userName;
    @NotEmpty
    private Set<String> roles;

    @NotEmpty private Set<InterestByUserDTO> interest;

    public UserDTO() {
    }

    public UserDTO(@NotNull Long id, @NotNull int age, @NotEmpty @Email String email, @NotEmpty String genre, @NotEmpty String userName, @NotEmpty Set<String> roles) {
        this.id = id;
        this.age = age;
        this.email = email;
        this.genre = genre;
        this.userName = userName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<InterestByUserDTO> getInterest() {
        return interest;
    }

    public void setInterest(Set<InterestByUserDTO> interest) {
        this.interest = interest;
    }
}
