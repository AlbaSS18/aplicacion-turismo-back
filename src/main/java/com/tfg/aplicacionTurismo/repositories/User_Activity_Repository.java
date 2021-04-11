package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface User_Activity_Repository extends CrudRepository<RelUserActivity, Long> {

    @Query("SELECT r FROM RelUserActivity r WHERE r.user = ?1 and r.activity = ?2")
    RelUserActivity findByUserAndActivity(User user, Activity activity);

    @Query("SELECT r FROM RelUserActivity r WHERE r.user = ?1")
    List<RelUserActivity> findByUser(User user);
}
