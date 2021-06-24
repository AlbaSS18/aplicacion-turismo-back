package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.rol.RolDTO;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.mapper.rol.RolMapper;
import com.tfg.aplicacionTurismo.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que responde a las acciones relacionadas con los roles.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/rol")
public class RolController {

    @Autowired
    private RolService rolService;

    /**
     * Método que devuelve la lista de roles.
     * @return la respuesta HTTP con la lista de roles.
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RolDTO>> getListRol(){
        List<Rol> rolList = rolService.getRoles();
        List<RolDTO> rolDTOList = new ArrayList<>();
        for(Rol rol: rolList){
            RolDTO rolDTO = RolMapper.INSTANCIA.convertRolToRolDTO(rol);
            rolDTOList.add(rolDTO);
        }
        return new ResponseEntity<List<RolDTO>>(rolDTOList, HttpStatus.OK);
    }
}
