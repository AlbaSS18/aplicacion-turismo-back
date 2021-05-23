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

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public void addCity(City city) {
        cityRepository.save(city);
    }

    public City getCityByNameCity(String name) {
        return cityRepository.findByNameCity(name);
    }

    public boolean existByName(String name){
        return cityRepository.existsByNameCity(name);
    }

    public boolean existById(Long id){
        return cityRepository.existsById(id);
    }

    public void deleteCity(Long id){
        cityRepository.deleteById(id);
    }

    public List<City> getCities(){
        List<City> cities = new ArrayList<>();
        cityRepository.findAll().forEach(cities::add);
        return cities;
    }

    public City getCityById(Long id){
        return cityRepository.findById(id).get();
    }

    public void updateCity(City city) {
        cityRepository.save(city);
    }

}
