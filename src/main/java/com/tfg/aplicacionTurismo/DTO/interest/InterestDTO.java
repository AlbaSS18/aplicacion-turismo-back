package com.tfg.aplicacionTurismo.DTO.interest;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class InterestDTO {

    @NotBlank
    private String nameInterest;
    @NotNull
    @Range(min=0,max=10)
    private int priority;

    public InterestDTO() {
    }

    public InterestDTO(@NotBlank String nameInterest, @NotNull @Range(min = 0, max = 10) int priority) {
        this.nameInterest = nameInterest;
        this.priority = priority;
    }

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
