package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.city.CityDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.city.NewCityDTO;
import com.tfg.aplicacionTurismo.DTO.interest.NewInterestDTO;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.services.CityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/add")
    public ResponseEntity<?> addCity(@RequestBody NewCityDTO newCityDTO){
        if(StringUtils.isBlank(newCityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("El nombre de la ciudad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(cityService.existByName(newCityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("Ya existe una ciudad con el mismo nombre"), HttpStatus.BAD_REQUEST);
        }
        City city = new City(newCityDTO.getName());
        cityService.addCity(city);
        return new ResponseEntity<>(new Mensaje("Ciudad creada"), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id){
        if(!cityService.existById(id)){
            return new ResponseEntity(new Mensaje("No existe una ciudad con el id " + id), HttpStatus.NOT_FOUND);
        }
        cityService.deleteCity(id);
        return new ResponseEntity(new Mensaje("Ciudad eliminada"), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CityDTO>> getCities(){
        List<City> listCities = cityService.getCities();
        List<CityDTO> listCitiesDTO = new ArrayList<>();
        for (City c: listCities){
            CityDTO cityDTO = new CityDTO();
            cityDTO.setId(c.getId());
            cityDTO.setName(c.getNameCity());
            listCitiesDTO.add(cityDTO);
        }
        return new ResponseEntity<List<CityDTO>>(listCitiesDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
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
