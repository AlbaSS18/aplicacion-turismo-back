package com.tfg.aplicacionTurismo.DTO;

import javax.validation.constraints.NotEmpty;

public class InterestListDTO {

    @NotEmpty
    private String nameInterest;

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }
}
