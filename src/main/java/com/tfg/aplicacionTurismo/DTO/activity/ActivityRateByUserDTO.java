package com.tfg.aplicacionTurismo.DTO.activity;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Clase que define los atributos del objeto que se utiliza para valorar una actividad recomendada.
 */
public class ActivityRateByUserDTO {
    @NotNull
    private Long activity_id;
    @NotBlank
    private String email_user;
    @NotNull
    @Range(min=0,max=5)
    private int rate;

    /**
     * Constructor de la clase ActivityRateByUserDTO
     */
    public ActivityRateByUserDTO() {
    }

    /**
     * Constructor de la clase ActivityRateByUserDTO
     * @param activity_id identificador de la actividad valorada
     * @param email_user email del usuario que valora la actividad
     * @param rate valoración dada por el usuario
     */
    public ActivityRateByUserDTO(@NotNull Long activity_id, @NotBlank String email_user, @NotNull @Range(min = 0, max = 5) int rate) {
        this.activity_id = activity_id;
        this.email_user = email_user;
        this.rate = rate;
    }

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
