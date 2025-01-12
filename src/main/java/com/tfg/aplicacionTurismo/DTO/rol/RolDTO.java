package com.tfg.aplicacionTurismo.DTO.rol;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para obtener rol/es.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class RolDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String rolName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }
}
