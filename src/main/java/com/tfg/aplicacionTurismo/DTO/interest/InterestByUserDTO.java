package com.tfg.aplicacionTurismo.DTO.interest;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class InterestByUserDTO {

    @NotEmpty private String user;
    @NotEmpty
    private Set<InterestDTO> interestByUser;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set<InterestDTO> getInterestByUser() {
        return interestByUser;
    }

    public void setInterestByUser(Set<InterestDTO> interestByUser) {
        this.interestByUser = interestByUser;
    }
}
