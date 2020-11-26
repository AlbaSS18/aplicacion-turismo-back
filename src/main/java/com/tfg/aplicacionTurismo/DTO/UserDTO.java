package com.tfg.aplicacionTurismo.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    private String password;
    @NotEmpty
    private String userName;

    public UserDTO() {
    }

    public UserDTO(@NotNull Long id, @NotNull int age, @NotEmpty @Email String email, @NotEmpty String genre, @NotEmpty String password, @NotEmpty String userName) {
        this.id = id;
        this.age = age;
        this.email = email;
        this.genre = genre;
        this.password = password;
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
