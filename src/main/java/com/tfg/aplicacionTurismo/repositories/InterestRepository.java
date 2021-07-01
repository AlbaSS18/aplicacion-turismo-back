package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a los tipos de intereses.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public interface InterestRepository extends CrudRepository<Interest, Long> {
    /**
     * Método que obtiene un tipo de interés a través de su nombre.
     * @param name nombre del tipo de interés.
     * @return el tipo de interés.
     */
    Interest findByNameInterest (String name);

    /**
     * Método que comprueba si existe un tipo de interés con un nombre concreto.
     * @param name nombre del tipo de interés.
     * @return true si existe el tipo de interés, false en caso contrario.
     */
    boolean existsByNameInterest(String name);
}
