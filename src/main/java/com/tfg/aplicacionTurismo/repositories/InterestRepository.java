package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InterestRepository extends CrudRepository<Interest, Long> {
    Interest findByNameInterest (String name);
    boolean existsByNameInterest(String name);
}
