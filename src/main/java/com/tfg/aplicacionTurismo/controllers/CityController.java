package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.city.CityDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.city.NewCityDTO;
import com.tfg.aplicacionTurismo.DTO.interest.NewInterestDTO;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.mapper.city.CityMapper;
import com.tfg.aplicacionTurismo.services.CityService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * Método que añade una nueva localidad.
     * @param newCityDTO objeto DTO que contiene la información necesaria para añadir una nueva localidad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return la respuesta HTTP que contiene un mensaje indicando que la nueva localidad se ha creado con éxito
     * o la respuesta HTTP que contiene un mensaje de error si los datos no son correctos o si ya existe una localidad con el nombre de la nueva localidad.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCity(@Validated @RequestBody NewCityDTO newCityDTO, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(cityService.existByName(newCityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("Ya existe una ciudad con el mismo nombre"), HttpStatus.BAD_REQUEST);
        }
        City city = new City(newCityDTO.getName());
        cityService.addCity(city);
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
    public ResponseEntity<?> deleteCity(@PathVariable Long id){
        if(!cityService.existById(id)){
            return new ResponseEntity(new Mensaje("No existe una ciudad con el id " + id), HttpStatus.NOT_FOUND);
        }
        try {
            cityService.deleteCity(id);
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
    public ResponseEntity<List<CityDTO>> getCities(){
        List<City> listCities = cityService.getCities();
        List<CityDTO> listCitiesDTO = new ArrayList<>();
        for (City c: listCities){
            CityDTO cityDTO = CityMapper.INSTANCIA.convertCityToCityDTO(c);
            listCitiesDTO.add(cityDTO);
        }
        return new ResponseEntity<List<CityDTO>>(listCitiesDTO, HttpStatus.OK);
    }

    /**
     * Método que modifica la información de una localidad a través de su identificador.
     * @param newCityDTO objeto DTO que contiene la información necesaria para actualizar una localidad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @param id indentificador de la localidad cuya información se quiere actualizar.
     * @return respuesta HTTP que contiene un mensaje indicando que la localidad se ha actualizado con éxito o
     * la respuesta HTTP que contiene un mensaje de error si los datos no son correctos, si no existe una localidad con ese identificador
     * o si ya existe una localidad con el nuevo nombre.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Validated @RequestBody NewCityDTO newCityDTO, BindingResult result, @PathVariable("id") Long id) {
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(!cityService.existById(id))
            return new ResponseEntity(new Mensaje("La ciudad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        if(cityService.existByName(newCityDTO.getName()) && cityService.getCityByNameCity(newCityDTO.getName()).getId() != id){
            return new ResponseEntity(new Mensaje("Ya hay una ciudad con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        City city = cityService.getCityById(id);
        city.setNameCity(newCityDTO.getName());
        cityService.updateCity(city);
        return new ResponseEntity(new Mensaje("Ciudad actualizada"), HttpStatus.CREATED);
    }

}
