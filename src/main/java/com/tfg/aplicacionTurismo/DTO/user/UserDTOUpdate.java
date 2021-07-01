package com.tfg.aplicacionTurismo.DTO.user;

import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Clase que define los atributos del objeto que se utiliza para actualizar la información de un usuario.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class UserDTOUpdate {

    @NotNull
    private Date dateBirthday;
    @NotBlank
    private String userName;
    @NotEmpty
    private Set<String> roles;

    @NotEmpty @Valid
    private Set<InterestByUserDTO> interest;

    /**
     * Constructor de la clase UserDTOUpdate.
     */
    public UserDTOUpdate() {
    }

    /**
     * Constructor de la clase UserDTOUpdate.
     * @param dateBirthday fecha de nacimiento.
     * @param userName nombre de usuario.
     * @param roles lista de roles del usuario.
     * @param interest lista de puntuaciones dadas por el usuario a los distintos tipos de intereses.
     */
    public UserDTOUpdate(@NotNull Date dateBirthday, @NotBlank String userName, @NotEmpty Set<String> roles, @NotEmpty @Valid Set<InterestByUserDTO> interest) {
        this.dateBirthday = dateBirthday;
        this.userName = userName;
        this.roles = roles;
        this.interest = interest;
    }

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
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
