package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.RelUserInterest;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface User_Interest_Repository extends CrudRepository<RelUserInterest, Long> {

    @Query("SELECT r FROM RelUserInterest r WHERE r.user = ?1")
    List<RelUserInterest> findAllByUser(User user);

    @Query("SELECT r FROM RelUserInterest r WHERE r.user = ?1 and r.interest = ?2")
    RelUserInterest findByUserAndInterest(User user, Interest interest);

    @Query("select case when count(r)> 0 then true else false end from RelUserInterest r where r.user = ?1 and r.interest = ?2")
    boolean existsByUserAndInterest(User user, Interest interest);
}
