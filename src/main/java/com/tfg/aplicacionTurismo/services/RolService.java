package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.RolName;
import com.tfg.aplicacionTurismo.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de roles que implementa la lógica de negocio y suministra datos al controlador.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    /**
     * Método que obtiene un tipo de rol concreto a través del repositorio utilizando su nombre.
     * @param rolName nombre del tipo de rol.
     * @return el tipo de rol.
     */
    public Rol getRolByRolName(RolName rolName){
        return rolRepository.findByrolName(rolName);
    }

    /**
     * Método que obtiene los tipos de roles a través del repositorio.
     * @return la lista de roles.
     */
    public List<Rol> getRoles (){
        List<Rol> roles = new ArrayList<>();
        rolRepository.findAll().forEach(roles::add);
        return roles;
    }
}
