package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.RolName;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol getRolByRolName(RolName rolName){
        return rolRepository.findByrolName(rolName);
    }

    public List<Rol> getRoles (){
        List<Rol> roles = new ArrayList<>();
        rolRepository.findAll().forEach(roles::add);
        return roles;
    }
}
