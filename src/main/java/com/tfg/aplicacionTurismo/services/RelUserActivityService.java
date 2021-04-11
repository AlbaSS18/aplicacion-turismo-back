package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.RelUserActivity;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.User_Activity_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelUserActivityService {

    @Autowired
    private User_Activity_Repository user_activity_repository;

    public RelUserActivity getValuationByUserAndActivity(User user, Activity activity) {
        return user_activity_repository.findByUserAndActivity(user, activity);
    }

    public List<RelUserActivity> getAllValuationByUser(User user) {
        return user_activity_repository.findByUser(user);
    }
}
