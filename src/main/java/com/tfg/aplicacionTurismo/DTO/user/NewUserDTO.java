package com.tfg.aplicacionTurismo.DTO.user;


import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

/**
 *  Clase que define los atributos del objeto que se utiliza para registrar a un usuario.
 */
public class NewUserDTO {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String userName;
    @NotBlank
    @Size(min=7)
    private String password;
    @NotBlank
    @Size(min=7)
    private String passwordConfirm;
    @NotNull
    private Date dateBirthday;

    @NotEmpty private Set<String> roles;

    @NotEmpty @Valid private Set<InterestDTO> interest;

    /**
     * Constructor de la clase NewUserDTO
     */
    public NewUserDTO() {
    }

    /**
     * Constructor de la clase NewUserDTO
     * @param email email del usuario
     * @param userName nombre de usuario
     * @param password contraseña
     * @param passwordConfirm contraseña confirmada
     * @param dateBirthday fecha de nacimiento
     * @param roles lista de roles del usuario
     * @param interest lista de puntuaciones dadas por el usuario a los tipos de interés
     */
    public NewUserDTO(@NotBlank @Email String email, @NotBlank String userName, @NotBlank @Size(min = 7) String password, @NotBlank @Size(min = 7) String passwordConfirm, @NotNull Date dateBirthday, @NotEmpty Set<String> roles, @NotEmpty @Valid Set<InterestDTO> interest) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.dateBirthday = dateBirthday;
        this.roles = roles;
        this.interest = interest;
    }

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
