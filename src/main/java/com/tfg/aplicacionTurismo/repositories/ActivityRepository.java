package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.Interest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
    boolean existsByName(String name);
}
