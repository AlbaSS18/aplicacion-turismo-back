package com.tfg.aplicacionTurismo.DTO.locality;

import javax.validation.constraints.NotBlank;

/**
 * Clase que define los atributos del objeto que se utiliza para crear o actualizar una nueva localidad.
 *
 * @author Alba Serena Suárez
 * @version 1.0
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
