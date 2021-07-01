package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotBlank;

/**
 * Clase que define los atributos del objeto que se utiliza para añadir o actualizar la información de los tipos de interés.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class NewInterestDTO {

    @NotBlank
    private String nameInterest;

    /**
     * Constructor de la clase NewInterestDTO.
     */
    public NewInterestDTO() {
    }

    public NewInterestDTO(@NotBlank String nameInterest) {
        this.nameInterest = nameInterest;
    }

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }
}
