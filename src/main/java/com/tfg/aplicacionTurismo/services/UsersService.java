package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de usuarios que implementa la lógica de negocio y suministra datos al controlador.
 */
@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
    }

    /**
     * Método que obtiene un usuario concreto a través del repositorio utilizando su email.
     * @param email correo electrónico del usuario.
     * @return el usuario.
     */
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    /**
     * Método que obtiene un usuario concreto a través del repositorio utilizando su id.
     * @param id identificador del usuario.
     * @return el usuario.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * Método que comprueba si existe un usuario a través del repositorio utilizando su email.
     * @param email correo electrónico del usuario.
     * @return true si el tipo de interés existe, false en caso contrario.
     */
    public boolean existsByEmail (String email){
        return userRepository.existsByEmail(email);
    }

    /**
     * Método que comprueba si existe un usuario a través del repositorio utilizando su id.
     * @param id identificador del usuario.
     * @return true si el tipo de interés existe, false en caso contrario.
     */
    public boolean existsById (Long id){
        return userRepository.existsById(id);
    }

    /**
     * Método que añade un nuevo usuario a través del repositorio.
     * @param user nuevo usuario.
     */
    public void addUser (User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Método que modifica la información de un usuario a través del repositorio.
     * @param user usuario con la información modificada.
     */
    public void updateUser (User user){
        userRepository.save(user);
    }

    /**
     * Método que obtiene los usuarios a través del repositorio.
     * @return la lista de usuarios.
     */
    public List<User> getUsers (){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    /**
     * Método que elimina un usuario a través del repositorio.
     * @param id identificador del usuario.
     */
    public void deleteUser (Long id){
        userRepository.deleteById(id);
    }
}
