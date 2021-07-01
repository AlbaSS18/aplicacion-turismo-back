package com.tfg.aplicacionTurismo.DTO.user;

import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;
import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Clase que define los atributos del objeto que se utiliza para obtener usuario/s.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class UserDTO {

    @NotNull
    private Long id;
    @NotNull
    private Date dateBirthday;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String userName;
    @NotEmpty
    private Set<String> roles;

    @NotEmpty @Valid
    private Set<InterestByUserDTO> interest;

    /**
     * Constructor de la clase UserDTO.
     */
    public UserDTO() {
    }

    /**
     * Constructor de la clase UserDTO.
     * @param id identificador del usuario.
     * @param dateBirthday fecha de nacimiento del usuario.
     * @param email email del usuario.
     * @param userName nombre de usuario.
     * @param roles lista de roles del usuario.
     */
    public UserDTO(@NotNull Long id, @NotNull Date dateBirthday, @NotEmpty @Email String email, @NotEmpty String userName, @NotEmpty Set<String> roles) {
        this.id = id;
        this.dateBirthday = dateBirthday;
        this.email = email;
        this.userName = userName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
