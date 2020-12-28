package com.tfg.aplicacionTurismo.DTO.rol;

import javax.validation.constraints.NotEmpty;

public class NewRolDTO {
    @NotEmpty
    private String rolName;

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }
}
