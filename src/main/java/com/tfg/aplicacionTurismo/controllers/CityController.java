package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.ActivityDTO;
import com.tfg.aplicacionTurismo.DTO.CityDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.services.CityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/add")
    public ResponseEntity<?> addCity(@RequestBody CityDTO cityDTO){
        if(StringUtils.isBlank(cityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("El nombre de la ciudad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(cityService.existByName(cityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("Ya existe una ciudad con el mismo nombre"), HttpStatus.BAD_REQUEST);
        }
        City city = new City(cityDTO.getName());
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
            cityDTO.setName(c.getNameCity());
            listCitiesDTO.add(cityDTO);
        }
        return new ResponseEntity<List<CityDTO>>(listCitiesDTO, HttpStatus.OK);
    }

}
