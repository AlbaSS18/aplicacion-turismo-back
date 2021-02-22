package com.tfg.aplicacionTurismo.DTO.user;


import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

public class NewUserDTO {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String userName;
    @NotEmpty
    @Size(min=7)
    private String password;
    @NotEmpty
    @Size(min=7)
    private String passwordConfirm;
    @NotNull
    private Date dateBirthday;

    @NotEmpty private Set<String> roles;

    @NotEmpty @Valid private Set<InterestDTO> interest;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<InterestDTO> getInterest() {
        return interest;
    }

    public void setInterest(Set<InterestDTO> interest) {
        this.interest = interest;
    }
}
