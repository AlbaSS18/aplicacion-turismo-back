package com.tfg.aplicacionTurismo.DTO.locality;

import javax.validation.constraints.NotBlank;

/**
 * Clase que define los atributos del objeto que se utiliza para crear o actualizar una nueva localidad.
 */
public class NewLocalityDTO {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
