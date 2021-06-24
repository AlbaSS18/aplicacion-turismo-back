package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a los usuarios.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Método que obtiene un usuario a través de su email.
     * @param email email del usuario
     * @return el usuario
     */
    User findByEmail (String email);

    /**
     * Método que comprueba si existe un usuario con un email concreto.
     * @param email email del usuario
     * @return true si existe el usuario, false en caso contrario
     */
    boolean existsByEmail(String email);
}
