package com.tfg.aplicacionTurismo.DTO;

import javax.validation.constraints.NotEmpty;

public class CityDTO {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
