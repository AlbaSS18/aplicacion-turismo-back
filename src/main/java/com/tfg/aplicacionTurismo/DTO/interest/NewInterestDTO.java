package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotBlank;

public class NewInterestDTO {

    @NotBlank
    private String nameInterest;

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }
}
