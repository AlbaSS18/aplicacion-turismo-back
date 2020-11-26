package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.InterestListDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.UserDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/details/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = usersService.getUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> listUser = usersService.getUsers();
        List<UserDTO> listUserDTO = new ArrayList<>();
        for(User user: listUser){
            UserDTO u = new UserDTO(user.getId(),user.getAge(),user.getEmail(),user.getGenre(),user.getPassword(),user.getUserName());
            listUserDTO.add(u);
        }
        return new ResponseEntity<List<UserDTO>>(listUserDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if(!usersService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("El usuario con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        // Comprobar que elimina tablas intermedias
        usersService.deleteUser(id);
        return new ResponseEntity<>(new Mensaje("Actividad eliminada"), HttpStatus.OK);
    }

    //ToDOMethod
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        if(!usersService.existsById(userDTO.getId())){
            return new ResponseEntity<>(new Mensaje("No existe el usuario con id " + id), HttpStatus.NOT_FOUND);
        }
        if(usersService.existsByEmail(userDTO.getEmail())){
            return new ResponseEntity<>(new Mensaje("Ya existe un usuario con el email " + userDTO.getEmail()), HttpStatus.NOT_FOUND);
        }
        User user = usersService.getUserById(id);
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setGenre(userDTO.getGenre());
        user.setUserName(userDTO.getUserName());

        usersService.addUser(user);
        return new ResponseEntity<>(new Mensaje("Usuario actualizado"), HttpStatus.CREATED);
    }

}
