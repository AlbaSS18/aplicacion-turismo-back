package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.RelUserActivity;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.User_Activity_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de valoraciones hechas por los usuarios a las actividades recomendadas que implementa la lógica de negocio y suministra datos al controlador.
 */
@Service
public class RelUserActivityService {

    @Autowired
    private User_Activity_Repository user_activity_repository;

    /**
     * Método que obtiene la valoración dada por un usuario a una actividad concreta a través del repositorio.
     * @param user usuario del cual se quiere obtener la valoración.
     * @param activity actividad de la cual se quiere obtener la valoración.
     * @return la valoración dada por el usuario a una actividad concreta.
     */
    public RelUserActivity getValuationByUserAndActivity(User user, Activity activity) {
        return user_activity_repository.findByUserAndActivity(user, activity);
    }

    /**
     * Método que obtiene la lista de valoraciones dadas por un usuario a las actividades a través del repositorio.
     * @param user usuario del cual se quiere obtener las valoraciones.
     * @return la lista de valoraciones hechas por el usuario.
     */
    public List<RelUserActivity> getAllValuationByUser(User user) {
        return user_activity_repository.findByUser(user);
    }

    /**
     * Método que obtiene la lista de valoraciones dadas por los usuarios a una actividad concreta a través del repositorio.
     * @param activity actividad de la que se quieren obtener las valoraciones.
     * @return la lista de valoraciones dadas por los usuarios a una actividad concreta.
     */
    public List<RelUserActivity> getAllValuationByActivity(Activity activity) {
        return user_activity_repository.findByActivity(activity);
    }
}
