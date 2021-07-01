package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para obtener interes/es.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class InterestListDTO {

    @NotBlank
    private String nameInterest;
    @NotNull
    private Long id;

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
