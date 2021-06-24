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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que responde a las acciones relacionadas con los intereses.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @Autowired
    private UsersService usersService;

    /**
     * Método que devuelve la lista de intereses.
     * @return la respuesta HTTP con la lista de intereses.
     */
    @GetMapping("/list")
    public ResponseEntity<List<InterestListDTO>> getListado() {
        List<Interest> list = interestService.getInterests();
        List<InterestListDTO> listDTO = new ArrayList<>();

        for(Interest interest: list){
            InterestListDTO i = InterestMapper.INSTANCIA.convertInterestToInterestListDTO(interest);
            listDTO.add(i);
        }
        return new ResponseEntity<List<InterestListDTO>>(listDTO, HttpStatus.OK);
    }

    /**
     * Método que añade un nuevo interés.
     * @param newInterestDTO objeto DTO que contiene la información necesaria para añadir un nuevo interés.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return la respuesta HTTP que contiene un mensaje indicando que el nuevo interés se ha creado con éxito
     * o la respuesta HTTP que contiene un mensaje de error si los datos no son correctos o si ya existe un interés con el nombre del nuevo interés.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addInterest(@Validated @RequestBody NewInterestDTO newInterestDTO, BindingResult result) {
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(interestService.existByName(newInterestDTO.getNameInterest())){
            return new ResponseEntity(new Mensaje("Ya hay un interés con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        Interest interest = InterestMapper.INSTANCIA.convertNewInterestToInterest(newInterestDTO);
        List<User> userList = usersService.getUsers();
        for(User u: userList){
            RelUserInterest relUserInterest = new RelUserInterest();
            relUserInterest.setInterest(interest);
            relUserInterest.setUser(u);
            relUserInterest.setPriority(0);
            interest.getPriority().add(relUserInterest);
            u.getPriority().add(relUserInterest);
        }
        interestService.addInterest(interest);
        return new ResponseEntity<>(new Mensaje("Interés creado"), HttpStatus.CREATED);
    }

    /**
     * Método que elimina un interés a través de su identificador.
     * @param id identificador del interés que se quiere eliminar.
     * @return la respuesta HTTP que contiene un mensaje indicando que el interés se ha eliminado con éxito
     * o la respuesta HTTP que contiene un mensaje de error si no existe un interés con ese identificador o si tiene actividades asociadas.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteInterest(@PathVariable Long id){
        if(!interestService.existById(id)){
            return new ResponseEntity<>(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }

        try {
            interestService.removeInterest(id);
        }catch (RuntimeException e){
            return new ResponseEntity(new Mensaje("El interés tiene actividades asociadas"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Mensaje("Interés eliminado"), HttpStatus.OK);
    }

    /**
     * Método que modifica la información de un interés a través de su identificador.
     * @param newInterestDTO objeto DTO que contiene la información necesaria para actualiar un interés.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @param id identificador del interés cuya información se quiere actualizar.
     * @return respuesta HTTP que contiene un mensaje indicando que el interés se ha actualizado con éxito o
     * la respuesta HTTP que contiene un mensaje de error si los datos no son correctos, si no existe un interés con ese identificador
     * o si ya existe un interés con el nuevo nombre.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Validated @RequestBody NewInterestDTO newInterestDTO, BindingResult result, @PathVariable("id") Long id) {
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(!interestService.existById(id))
            return new ResponseEntity(new Mensaje("El interés con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        if(interestService.existByName(newInterestDTO.getNameInterest()) && interestService.getInterestByName(newInterestDTO.getNameInterest()).getId() != id){
            return new ResponseEntity(new Mensaje("Ya hay un interés con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        Interest interest = interestService.getInterestById(id);
        interest.setNameInterest(newInterestDTO.getNameInterest());
        interestService.updateInterest(interest);
        return new ResponseEntity(new Mensaje("Interés actualizado"), HttpStatus.CREATED);
    }
}
