package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.locality.LocalityDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.locality.NewLocalityDTO;
import com.tfg.aplicacionTurismo.entities.Locality;
import com.tfg.aplicacionTurismo.mapper.locality.LocalityMapper;
import com.tfg.aplicacionTurismo.services.LocalityService;
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
 * Clase que responde a las acciones relacionadas con las localidades.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/locality")
public class LocalityController {

    @Autowired
    private LocalityService localityService;

    /**
     * Método que añade una nueva localidad.
     * @param newLocalityDTO objeto DTO que contiene la información necesaria para añadir una nueva localidad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return la respuesta HTTP que contiene un mensaje indicando que la nueva localidad se ha creado con éxito
     * o la respuesta HTTP que contiene un mensaje de error si los datos no son correctos o si ya existe una localidad con el nombre de la nueva localidad.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addLocality(@Validated @RequestBody NewLocalityDTO newLocalityDTO, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(localityService.existByName(newLocalityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("Ya existe una ciudad con el mismo nombre"), HttpStatus.BAD_REQUEST);
        }
        Locality locality = new Locality(newLocalityDTO.getName());
        localityService.addLocality(locality);
        return new ResponseEntity<>(new Mensaje("Ciudad creada"), HttpStatus.CREATED);
    }

    /**
     * Método que elimina una localidad a través de su identificador.
     * @param id identificador de la localidad que se quiere eliminar.
     * @return la respuesta HTTP que contiene un mensaje indicando que la localidad se ha eliminado con éxito
     * o la respuesta HTTP que contiene un mensaje de error si no existe una localidad con ese identificador o si tiene actividades asociadas.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLocality(@PathVariable Long id){
        if(!localityService.existById(id)){
            return new ResponseEntity(new Mensaje("No existe una ciudad con el id " + id), HttpStatus.NOT_FOUND);
        }
        try {
            localityService.deleteLocality(id);
        }catch (RuntimeException e){
            return new ResponseEntity(new Mensaje("La ciudad tiene actividades asociadas"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new Mensaje("Ciudad eliminada"), HttpStatus.OK);
    }

    /**
     * Método que devuelve la lista de localidades.
     * @return la respuesta HTTP con la lista de localidades.
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LocalityDTO>> getLocalities(){
        List<Locality> listLocalities = localityService.getLocalities();
        List<LocalityDTO> listLocalitiesDTO = new ArrayList<>();
        for (Locality c: listLocalities){
            LocalityDTO localityDTO = LocalityMapper.INSTANCIA.convertLocalityToLocalityDTO(c);
            listLocalitiesDTO.add(localityDTO);
        }
        return new ResponseEntity<List<LocalityDTO>>(listLocalitiesDTO, HttpStatus.OK);
    }

    /**
     * Método que modifica la información de una localidad a través de su identificador.
     * @param newLocalityDTO objeto DTO que contiene la información necesaria para actualizar una localidad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @param id indentificador de la localidad cuya información se quiere actualizar.
     * @return respuesta HTTP que contiene un mensaje indicando que la localidad se ha actualizado con éxito o
     * la respuesta HTTP que contiene un mensaje de error si los datos no son correctos, si no existe una localidad con ese identificador
     * o si ya existe una localidad con el nuevo nombre.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Validated @RequestBody NewLocalityDTO newLocalityDTO, BindingResult result, @PathVariable("id") Long id) {
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(!localityService.existById(id))
            return new ResponseEntity(new Mensaje("La ciudad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        if(localityService.existByName(newLocalityDTO.getName()) && localityService.getLocalityByNameLocality(newLocalityDTO.getName()).getId() != id){
            return new ResponseEntity(new Mensaje("Ya hay una ciudad con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        Locality locality = localityService.getLocalityById(id);
        locality.setNameLocality(newLocalityDTO.getName());
        localityService.updateLocality(locality);
        return new ResponseEntity(new Mensaje("Ciudad actualizada"), HttpStatus.CREATED);
    }

}
