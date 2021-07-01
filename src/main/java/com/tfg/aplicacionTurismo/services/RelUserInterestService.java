package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.RelUserInterest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.InterestRepository;
import com.tfg.aplicacionTurismo.repositories.User_Interest_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  Servicio de puntuaciones dadas por los usuarios a los distintos tipos de intereses que implementa la lógica de negocio y suministra datos al controlador.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Service
public class RelUserInterestService {

    @Autowired
    private User_Interest_Repository user_interest_repository;

    /**
     * Método que obtiene la puntuación dada por un usuario a un tipo de interés concreto a través del repositorio.
     * @param user usuario del cual se quiere obtener la puntuación.
     * @param interest interés del cual se quiere obtener la puntuación.
     * @return la puntuación dada por el usuario a un tipo de interés concreto.
     */
    public RelUserInterest getInterestByUserAndInterest(User user, Interest interest) {
        return user_interest_repository.findByUserAndInterest(user, interest);
    }

    /**
     * Método que comprueba si un usuario ha puntuado un tipo de interés concreto a través del repositorio.
     * @param user usuario del cual se quiere obtener las puntuaciones.
     * @param interest interés del cual se quiere obtener las puntuaciones.
     * @return true si el usuario ha puntuado el interés, false en caso contrario
     */
    public boolean existByUserAndInterest(User user, Interest interest) {
        return user_interest_repository.existsByUserAndInterest(user, interest);
    }

    /**
     * Método que obtiene la lista de puntuaciones dadas por un usuario a los distintos tipos de interés a través del repositorio.
     * @param user usuario del cual se quiere obtener las puntuaciones.
     * @return la lista de puntuaciones dadas por un usuario a los distintos tipos de interés.
     */
    public List<RelUserInterest> getAllPriorityByUser(User user) {
        return user_interest_repository.findByUser(user);
    }
}
