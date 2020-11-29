package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.RolDTO;
import com.tfg.aplicacionTurismo.DTO.UserDTO;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.mapper.rol.RolMapper;
import com.tfg.aplicacionTurismo.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/rol")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("/list")
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
