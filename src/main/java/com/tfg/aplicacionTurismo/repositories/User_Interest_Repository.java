package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.RelUserInterest;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a los puntuaciones dadas por los usuarios a los distintos tipos de intereses.
 */
public interface User_Interest_Repository extends CrudRepository<RelUserInterest, Long> {

    /**
     * Método que obtiene la puntuación dada por un usuario a un tipo de interés concreto.
     * @param user usuario del cual se quiere obtener la puntuación.
     * @param interest interés del cual se quiere obtener la puntuación.
     * @return la puntuación dada por el usuario a un tipo de interés concreto.
     */
    @Query("SELECT r FROM RelUserInterest r WHERE r.user = ?1 and r.interest = ?2")
    RelUserInterest findByUserAndInterest(User user, Interest interest);

    /**
     * Método que comprueba si un usuario ha puntuado un tipo de interés concreto.
     * @param user usuario del cual se quiere obtener las puntuaciones.
     * @param interest interés del cual se quiere obtener las puntuaciones.
     * @return true si el usuario ha puntuado el interés, false en caso contrario
     */
    @Query("select case when count(r)> 0 then true else false end from RelUserInterest r where r.user = ?1 and r.interest = ?2")
    boolean existsByUserAndInterest(User user, Interest interest);

    /**
     * Método que obtiene la lista de puntuaciones dadas por un usuario a los distintos tipos de interés.
     * @param user usuario del cual se quiere obtener las puntuaciones.
     * @return la lista de puntuaciones dadas por un usuario a los distintos tipos de interés.
     */
    @Query("SELECT r FROM RelUserInterest r WHERE r.user = ?1")
    List<RelUserInterest> findByUser(User user);
}
