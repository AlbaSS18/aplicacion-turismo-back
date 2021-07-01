package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a los valoraciones hechas por los usuarios a las actividades recomendadas.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public interface User_Activity_Repository extends CrudRepository<RelUserActivity, Long> {

    /**
     * Método que obtiene la valoración dada por un usuario a una actividad concreta.
     * @param user usuario del cual se quiere obtener la valoración.
     * @param activity actividad de la cual se quiere obtener la valoración.
     * @return la valoración dada por el usuario a una actividad concreta.
     */
    @Query("SELECT r FROM RelUserActivity r WHERE r.user = ?1 and r.activity = ?2")
    RelUserActivity findByUserAndActivity(User user, Activity activity);

    /**
     * Método que obtiene la lista de valoraciones dadas por un usuario a las actividades.
     * @param user usuario del cual se quiere obtener las valoraciones.
     * @return la lista de valoraciones hechas por el usuario.
     */
    @Query("SELECT r FROM RelUserActivity r WHERE r.user = ?1")
    List<RelUserActivity> findByUser(User user);

    /**
     * Método que obtiene la lista de valoraciones dadas por los usuarios a una actividad concreta.
     * @param activity actividad de la que se quieren obtener las valoraciones.
     * @return la lista de valoraciones dadas por los usuarios a una actividad concreta.
     */
    @Query("SELECT r FROM RelUserActivity r WHERE r.activity = ?1")
    List<RelUserActivity> findByActivity(Activity activity);
}
