package com.tfg.aplicacionTurismo.DTO.city;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewCityDTO {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
