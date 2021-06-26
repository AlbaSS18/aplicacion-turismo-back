package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.repositories.CityRepository;
import com.tfg.aplicacionTurismo.repositories.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de localidades que implementa la lógica de negocio y suministra datos al controlador.
 */
@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    /**
     * Método que añade una nueva localidad a través del repositorio.
     * @param city nueva localidad.
     */
    public void addCity(City city) {
        cityRepository.save(city);
    }

    /**
     * Método que obtiene una localidad concreta a través del repositorio utilizando su nombre.
     * @param name nombre de la localidad.
     * @return la localidad.
     */
    public City getCityByNameCity(String name) {
        return cityRepository.findByNameCity(name);
    }

    /**
     * Método que comprueba si existe una localidad a través del repositorio utilizando su nombre.
     * @param name nombre de la localidad.
     * @return true si la localidad existe, false en caso contrario.
     */
    public boolean existByName(String name){
        return cityRepository.existsByNameCity(name);
    }

    /**
     * Método que comprueba si existe una localidad a través del repositorio utilizando su id.
     * @param id identificador de la localidad.
     * @return true si la localidad existe, false en caso contrario.
     */
    public boolean existById(Long id){
        return cityRepository.existsById(id);
    }

    /**
     * Método que elimina una localidad a través del repositorio.
     * @param id identificador de la localidad que se quiere eliminar.
     */
    public void deleteCity(Long id){
        cityRepository.deleteById(id);
    }

    /**
     * Método que obtiene las localidades a través del repositorio.
     * @return la lista de localidades.
     */
    public List<City> getCities(){
        List<City> cities = new ArrayList<>();
        cityRepository.findAll().forEach(cities::add);
        return cities;
    }

    /**
     * Método que obtiene una localidad concreta a través del repositorio utilizando su id.
     * @param id identificador de la localidad.
     * @return la localidad.
     */
    public City getCityById(Long id){
        return cityRepository.findById(id).get();
    }

    /**
     * Método que modifica la información de una localidad a través del repositorio.
     * @param city localidad
     */
    public void updateCity(City city) {
        cityRepository.save(city);
    }
}
