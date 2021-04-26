package com.tfg.aplicacionTurismo.DTO.activity;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ActivityRateByUserDTO {
    @NotNull
    private Long activity_id;
    @NotBlank
    private String email_user;
    @NotNull
    @Range(min=0,max=5)
    private int rate;

    public Long getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Long activity_id) {
        this.activity_id = activity_id;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
