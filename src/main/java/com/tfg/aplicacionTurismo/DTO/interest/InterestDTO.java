package com.tfg.aplicacionTurismo.DTO.interest;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Clase que define los atributos del objeto que se utiliza para puntuar los tipos de interés cuando se registra un usuario.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class InterestDTO {

    @NotBlank
    private String nameInterest;
    @NotNull
    @Range(min=0,max=10)
    private int priority;

    /**
     * Constructor de la clase InterestDTO.
     */
    public InterestDTO() {
    }

    /**
     * Constructor de la clase InterestDTO.
     * @param nameInterest nombre del tipo de interés.
     * @param priority puntuación dada.
     */
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
