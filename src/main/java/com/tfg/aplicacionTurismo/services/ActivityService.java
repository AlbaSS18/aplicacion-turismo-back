package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.repositories.ActivityRepository;
import com.tfg.aplicacionTurismo.repositories.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de actividades que implementa la lógica de negocio y suministra datos al controlador.
 */
@Service
public class ActivityService {


    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Método que obtiene las actividades a través del repositorio.
     * @return la lista de actividades
     */
    public List<Activity> getActivities(){
        List<Activity> activities = new ArrayList<Activity>();
        activityRepository.findAll().forEach(activities::add);
        return activities;
    }

    /**
     * Método que añade una nueva actividad a través del repositorio.
     * @param activity nueva actividad
     */
    public void addActivities(Activity activity){
        activityRepository.save(activity);
    }

    /**
     * Método que elimina una actividad a través del repositorio.
     * @param id identificador de la actividad que se quiere eliminar.
     */
    public void removeActivities(Long id){
        activityRepository.deleteById(id);
    }

    /**
     * Método que comprueba si existe una actividad a través del repositorio utilizando su id.
     * @param id identificador de la actividad.
     * @return true si la actividad existe, false en caso contrario.
     */
    public boolean existsById(Long id){
        return activityRepository.existsById(id);
    }

    /**
     * Método que comprueba si existe una actividad a través del repositorio utilizando su nombre.
     * @param name nombre de la actividad.
     * @return true si la actividad existe, false en caso contrario.
     */
    public boolean existsByName(String name){
        return activityRepository.existsByName(name);
    }

    /**
     * Método que obtiene una actividad concreto a través del repositorio utilizando su id.
     * @param id identificador de la actividad.
     * @return la actividad.
     */
    public Activity getById(Long id){
        return activityRepository.findById(id).get();
    }

    /**
     * Método que modifica la información de una actividad a través del repositorio.
     * @param activity actividad con la información modificada.
     */
    public void updateActivity(Activity activity){
        activityRepository.save(activity);
    }

    /**
     * Método que obtiene una actividad concreta a través del repositorio utilizando su nombre.
     * @param name nombre de la actividad.
     * @return la actividad.
     */
    public Activity getActivityByNameActivity(String name){
        return activityRepository.findByName(name);
    }
}
