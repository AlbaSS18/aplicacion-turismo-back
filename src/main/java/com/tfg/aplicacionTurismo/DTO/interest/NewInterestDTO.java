package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotEmpty;

public class NewInterestDTO {

    @NotEmpty
    private String nameInterest;

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }
}
