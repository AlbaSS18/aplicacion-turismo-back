package com.tfg.aplicacionTurismo.DTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class InterestDTO {

    @NotEmpty
    private String nameInterest;
    @NotEmpty private int priority;

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
