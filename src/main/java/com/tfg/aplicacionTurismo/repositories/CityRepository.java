package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository  extends CrudRepository<City, Long> {
    City findByNameCity (String name);
    boolean existsByNameCity(String name);
}
