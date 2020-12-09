package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InterestListDTO {

    @NotEmpty
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
