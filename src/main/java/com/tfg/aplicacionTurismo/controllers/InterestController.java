package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;
import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import com.tfg.aplicacionTurismo.DTO.interest.InterestListDTO;
import com.tfg.aplicacionTurismo.DTO.interest.NewInterestDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.RelUserInterest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.mapper.interest.InterestMapper;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.RelUserInterestService;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RelUserInterestService relUserInterestService;

    @GetMapping("/list")
    public ResponseEntity<List<InterestListDTO>> getListado() {
        List<Interest> list = interestService.getInterests();
        System.out.println(list);
        List<InterestListDTO> listDTO = new ArrayList<>();
        for(Interest interest: list){
            InterestListDTO i = new InterestListDTO();
            i.setNameInterest(interest.getNameInterest());
            i.setId(interest.getId());
            listDTO.add(i);
        }
        return new ResponseEntity<List<InterestListDTO>>(listDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addInterest(@Validated @RequestBody NewInterestDTO newInterestDTO, BindingResult result) {
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(interestService.existByName(newInterestDTO.getNameInterest())){
            return new ResponseEntity(new Mensaje("Ya hay un interés con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        Interest interest = InterestMapper.INSTANCIA.convertNewInterestInInterest(newInterestDTO);
        interestService.addInterest(interest);
        return new ResponseEntity<>(new Mensaje("Actividad creada"), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInterest(@PathVariable Long id){
        if(!interestService.existById(id)){
            return new ResponseEntity<>(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        //Comprobar que no tiene actividades relacionadas
        interestService.removeInterest(id);
        return new ResponseEntity<>(new Mensaje("Actividad eliminada"), HttpStatus.OK);
    }

    //Añadir anotación @PreAuthorize("hasRola(''ADMIN)") para aquellas que sólo puedan ser vistas por el admin
}
