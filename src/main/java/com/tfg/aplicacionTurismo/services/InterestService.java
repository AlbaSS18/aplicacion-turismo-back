package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.InterestRepository;
import com.tfg.aplicacionTurismo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Servicio de intereses que implementa la lógica de negocio y suministra datos al controlador.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    /**
     * Método que obtiene los tipos de intereses a través del repositorio.
     * @return la lista de intereses.
     */
    public List<Interest> getInterests() {
        List<Interest> interests = new ArrayList<Interest>();
        interestRepository.findAll().forEach(interests::add);
        return interests;
    }

    /**
     * Método que obtiene un tipo de interés concreto a través del repositorio utilizando su nombre.
     * @param name nombre del tipo de interés.
     * @return el tipo de interés.
     */
    public Interest getInterestByName(String name) {
        return interestRepository.findByNameInterest(name);
    }

    /**
     * Método que comprueba si existe un tipo de interés a través del repositorio utilizando su nombre.
     * @param name nombre del tipo de interés.
     * @return true si el tipo de interés existe, false en caso contrario.
     */
    public boolean existByName(String name) {
        return interestRepository.existsByNameInterest(name);
    }

    /**
     * Método que añade un nuevo tipo de interés a través del repositorio.
     * @param interest nuevo tipo de interés.
     */
    public void addInterest(Interest interest){
        interestRepository.save(interest);
    }

    /**
     * Método que comprueba si existe un tipo de interés a través del repositorio utilizando su id.
     * @param id identificador del tipo de interés.
     * @return true si el tipo de interés existe, false en caso contrario.
     */
    public boolean existById(Long id){
        return interestRepository.existsById(id);
    }

    /**
     * Método que elimina un tipo de interés a través del repositorio.
     * @param id identificador del tipo de interés que se quiere eliminar.
     */
    public void removeInterest(Long id){
        interestRepository.deleteById(id);
    }

    /**
     * Método que obtiene un tipo de interés concreto a través del repositorio utilizando su id.
     * @param id identificador del tipo de interés.
     * @return el tipo de interés.
     */
    public Interest getInterestById (Long id) {
        return interestRepository.findById(id).get();
    }

    /**
     * Método que modifica la información de un tipo de interés a través del repositorio.
     * @param interest tipo de interés con la información modificada.
     */
    public void updateInterest(Interest interest){
        interestRepository.save(interest);
    }
}
