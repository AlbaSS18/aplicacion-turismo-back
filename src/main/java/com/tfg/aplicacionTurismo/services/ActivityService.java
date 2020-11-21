package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.repositories.ActivityRepository;
import com.tfg.aplicacionTurismo.repositories.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> getActivities(){
        List<Activity> activities = new ArrayList<Activity>();
        activityRepository.findAll().forEach(activities::add);
        return activities;
    }

    public void addActivities(Activity activity){
        activityRepository.save(activity);
    }

    public void removeActivities(Long id){
        activityRepository.deleteById(id);
    }

    public boolean existsById(Long id){
        return activityRepository.existsById(id);
    }
}
