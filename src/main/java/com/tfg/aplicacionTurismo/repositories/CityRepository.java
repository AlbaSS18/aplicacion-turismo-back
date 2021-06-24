package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a las localidades.
 */
public interface CityRepository  extends CrudRepository<City, Long> {
    /**
     * Método que obtiene una localidad a través de su nombre.
     * @param name nombre de la localidad
     * @return la localidad
     */
    City findByNameCity (String name);

    /**
     * Método que comprueba si existe una localidad con un nombre concreto.
     * @param name nombre de la localidad
     * @return true si existe la localidad, false en caso contrario
     */
    boolean existsByNameCity(String name);
}
