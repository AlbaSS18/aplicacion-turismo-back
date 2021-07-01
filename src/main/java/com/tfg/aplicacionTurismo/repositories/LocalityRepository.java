package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Locality;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a las localidades.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public interface LocalityRepository extends CrudRepository<Locality, Long> {
    /**
     * Método que obtiene una localidad a través de su nombre.
     * @param name nombre de la localidad.
     * @return la localidad.
     */
    Locality findByNameLocality(String name);

    /**
     * Método que comprueba si existe una localidad con un nombre concreto.
     * @param name nombre de la localidad.
     * @return true si existe la localidad, false en caso.
     */
    boolean existsByNameLocality(String name);
}
