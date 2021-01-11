package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class InterestByUserDTO {

    @NotNull
    private Long interestID;
    @NotEmpty
    private String nameInterest;
    @NotEmpty private int priority;

    public Long getInterestID() {
        return interestID;
    }

    public void setInterestID(Long interestID) {
        this.interestID = interestID;
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
