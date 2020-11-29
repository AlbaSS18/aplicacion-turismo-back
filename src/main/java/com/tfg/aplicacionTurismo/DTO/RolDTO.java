package com.tfg.aplicacionTurismo.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RolDTO {
    @NotNull
    private Long id;
    @NotEmpty
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
