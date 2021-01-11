package com.tfg.aplicacionTurismo.repositories;

import com.tfg.aplicacionTurismo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail (String email);
    boolean existsByEmail(String email);
}
