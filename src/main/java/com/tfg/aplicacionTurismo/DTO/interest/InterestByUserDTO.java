package com.tfg.aplicacionTurismo.DTO.interest;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Clase que define los atributos del objeto que se utiliza para obtener la puntuaciones realizadas por un usuario o para puntuar los tipos de interés.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class InterestByUserDTO {

    @NotNull
    private Long interestID;
    @NotBlank
    private String nameInterest;
    @NotNull
    @Range(min=0,max=10)
    private int priority;

    /**
     * Constructor de la clase InterestByUserDTO.
     */
    public InterestByUserDTO() {
    }

    /**
     * Constructor de la clase InterestByUserDTO.
     * @param interestID identificador del tipo de interés.
     * @param nameInterest nombre del tipo de interés.
     * @param priority puntuación dada.
     */
    public InterestByUserDTO(@NotNull Long interestID, @NotBlank String nameInterest, @NotNull @Range(min = 0, max = 10) int priority) {
        this.interestID = interestID;
        this.nameInterest = nameInterest;
        this.priority = priority;
    }

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
