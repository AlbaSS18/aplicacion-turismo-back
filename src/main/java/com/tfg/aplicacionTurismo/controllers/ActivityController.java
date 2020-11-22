package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.ActivityDTO;
import com.tfg.aplicacionTurismo.DTO.InterestListDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.services.ActivityService;
import com.tfg.aplicacionTurismo.services.CityService;
import com.tfg.aplicacionTurismo.services.InterestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Controller
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CityService cityService;

    @Autowired
    private InterestService interestService;

    @GetMapping("/list")
    public ResponseEntity<List<ActivityDTO>> getListado(){
        List<Activity> listActivity = activityService.getActivities();
        List<ActivityDTO> listDTO = new ArrayList<>();
        for(Activity activity: listActivity){
            ActivityDTO i = new ActivityDTO(activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest());
            listDTO.add(i);
        }
        return new ResponseEntity<List<ActivityDTO>>(listDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addActivity(@RequestBody ActivityDTO activityDTO){
        if(StringUtils.isEmpty(activityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("El nombre de la actividad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getDescription())){
            return new ResponseEntity<>(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("El nombre de la ciudad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(!cityService.existByName(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("La ciudad no existe"), HttpStatus.NOT_FOUND);
        }
        if(StringUtils.isEmpty(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El nombre del interés es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(!interestService.existByName(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El interés no existe"), HttpStatus.NOT_FOUND);
        }
        if(activityService.existsByName(activityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("Ya existe una actividad con el nombre: " + activityDTO.getName()), HttpStatus.BAD_REQUEST);
        }
        Activity activity = new Activity(activityDTO.getName(), activityDTO.getDescription(), new Point(activityDTO.getLongitude(), activityDTO.getLatitude()), activityDTO.getPathImage());
        City city = cityService.getCityByNameCity(activityDTO.getCity());
        activity.setCity(city);
        Interest interest = interestService.getInterestByName(activityDTO.getInterest());
        activity.setInterest(interest);
        activityService.addActivities(activity);
        return new ResponseEntity<>(new Mensaje("Actividad creada"), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id){
        if(!activityService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        //Comprobar que no tiene usuarios asociados
        activityService.removeActivities(id);
        return new ResponseEntity<>(new Mensaje("Actividad eliminada"), HttpStatus.OK);
    }
}
