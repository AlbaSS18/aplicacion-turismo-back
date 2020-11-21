package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/details/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = usersService.getUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
