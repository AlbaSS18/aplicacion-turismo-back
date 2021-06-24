package com.tfg.aplicacionTurismo.DTO.city;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para obtener localidad/es.
 */
public class CityDTO {

    @NotBlank
    private String name;
    @NotNull
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
