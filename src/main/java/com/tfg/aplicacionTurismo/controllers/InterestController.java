package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.InterestDTO;
import com.tfg.aplicacionTurismo.DTO.InterestListDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.RelUserInterest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.services.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @GetMapping("/list")
    public ResponseEntity<List<InterestListDTO>> getListado() {
        List<Interest> list = interestService.getInterests();
        System.out.println(list);
        List<InterestListDTO> listDTO = new ArrayList<>();
        for(Interest interest: list){
            System.out.println(interest.getNameInterest());
            InterestListDTO i = new InterestListDTO();
            i.setNameInterest(interest.getNameInterest());
            listDTO.add(i);
        }
        return new ResponseEntity<List<InterestListDTO>>(listDTO, HttpStatus.OK);
    }

    //Añadir anotación @PreAuthorize("hasRola(''ADMIN)") para aquellas que sólo puedan ser vistas por el admin
}
