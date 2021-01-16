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

@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    public List<Interest> getInterests() {
        List<Interest> interests = new ArrayList<Interest>();
        interestRepository.findAll().forEach(interests::add);
        return interests;
    }

    public Interest getInterestByName(String name) {
        return interestRepository.findByNameInterest(name);
    }

    public boolean existByName(String name) {
        return interestRepository.existsByNameInterest(name);
    }

    public void addInterest(Interest interest){
        interestRepository.save(interest);
    }

    public boolean existById(Long id){
        return interestRepository.existsById(id);
    }

    public void removeInterest(Long id){
        interestRepository.deleteById(id);
    }

    public Interest getInterestById (Long id) {
        return interestRepository.findById(id).get();
    }

    public void updateInterest(Interest interest){
        interestRepository.save(interest);
    }
}
