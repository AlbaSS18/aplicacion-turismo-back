package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.RelUserInterest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.InterestRepository;
import com.tfg.aplicacionTurismo.repositories.User_Interest_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelUserInterestService {

    @Autowired
    private User_Interest_Repository user_interest_repository;

    public RelUserInterest getInterestByUserAndInterest(User user, Interest interest) {
        return user_interest_repository.findByUserAndInterest(user, interest);
    }

    public boolean existByUserAndInterest(User user, Interest interest) {
        return user_interest_repository.existsByUserAndInterest(user, interest);
    }

    public List<RelUserInterest> getAllPriorityByUser(User user) {
        return user_interest_repository.findByUser(user);
    }
}
