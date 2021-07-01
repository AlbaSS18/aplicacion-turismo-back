package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Locality;
import com.tfg.aplicacionTurismo.repositories.LocalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de localidades que implementa la lógica de negocio y suministra datos al controlador.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Service
public class LocalityService {

    @Autowired
    private LocalityRepository localityRepository;

    /**
     * Método que añade una nueva localidad a través del repositorio.
     * @param locality nueva localidad.
     */
    public void addLocality(Locality locality) {
        localityRepository.save(locality);
    }

    /**
     * Método que obtiene una localidad concreta a través del repositorio utilizando su nombre.
     * @param name nombre de la localidad.
     * @return la localidad.
     */
    public Locality getLocalityByNameLocality(String name) {
        return localityRepository.findByNameLocality(name);
    }

    /**
     * Método que comprueba si existe una localidad a través del repositorio utilizando su nombre.
     * @param name nombre de la localidad.
     * @return true si la localidad existe, false en caso contrario.
     */
    public boolean existByName(String name){
        return localityRepository.existsByNameLocality(name);
    }

    /**
     * Método que comprueba si existe una localidad a través del repositorio utilizando su id.
     * @param id identificador de la localidad.
     * @return true si la localidad existe, false en caso contrario.
     */
    public boolean existById(Long id){
        return localityRepository.existsById(id);
    }

    /**
     * Método que elimina una localidad a través del repositorio.
     * @param id identificador de la localidad que se quiere eliminar.
     */
    public void deleteLocality(Long id){
        localityRepository.deleteById(id);
    }

    /**
     * Método que obtiene las localidades a través del repositorio.
     * @return la lista de localidades.
     */
    public List<Locality> getLocalities(){
        List<Locality> localities = new ArrayList<>();
        localityRepository.findAll().forEach(localities::add);
        return localities;
    }

    /**
     * Método que obtiene una localidad concreta a través del repositorio utilizando su id.
     * @param id identificador de la localidad.
     * @return la localidad.
     */
    public Locality getLocalityById(Long id){
        return localityRepository.findById(id).get();
    }

    /**
     * Método que modifica la información de una localidad a través del repositorio.
     * @param locality localidad
     */
    public void updateLocality(Locality locality) {
        localityRepository.save(locality);
    }
}
