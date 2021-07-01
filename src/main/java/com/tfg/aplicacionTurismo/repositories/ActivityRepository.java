package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.Interest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a las actividades.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public interface ActivityRepository extends CrudRepository<Activity, Long> {
    /**
     * Método que comprueba si existe una actividad con un nombre concreto.
     * @param name nombre de la actividad.
     * @return true si existe la actividad, false en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Método que obtiene una actividad a través de su nombre.
     * @param name nombre de la actividad.
     * @return la actividad.
     */
    Activity findByName(String name);
}
