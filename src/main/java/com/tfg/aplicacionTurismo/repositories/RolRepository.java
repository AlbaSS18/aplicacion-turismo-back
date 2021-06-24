package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.RolName;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface que define el mecanismo necesario para gestionar el acceso a la información relativa a los tipos de rol web.
 */
public interface RolRepository extends CrudRepository<Rol, Long> {
    /**
     * Método que obtiene un tipo de rol a través de su nombre.
     * @param rolName nombre del tipo de rol
     * @return el rol
     */
    Rol findByrolName (RolName rolName);
}
