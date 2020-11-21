package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.RolName;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.repository.CrudRepository;


public interface RolRepository extends CrudRepository<Rol, Long> {
    Rol findByrolName (RolName rolName);
}
