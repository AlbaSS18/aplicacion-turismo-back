package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.user.UserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTOUpdate;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.mapper.user.UserMapper;
import com.tfg.aplicacionTurismo.services.RolService;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolService rolService;

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        if(!usersService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("El usuario con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        User user = usersService.getUserById(id);
        UserDTO userDTO = UserMapper.INSTANCIA.convertUserToUserDTO(user);
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> listUser = usersService.getUsers();
        List<UserDTO> listUserDTO = new ArrayList<>();
        for(User user: listUser){
            Set<Rol> roles = user.getRole();
            Set<String> rolString = new HashSet<>();
            for (Rol rol: roles){
                rolString.add(rol.getRolName().name());
            }
            UserDTO u = new UserDTO(user.getId(),user.getAge(),user.getEmail(),user.getGenre(),user.getUserName(), rolString);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@Validated @RequestBody UserDTOUpdate userDTOUpdate, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Rellenar todos los datos"), HttpStatus.BAD_REQUEST);
        }
        if(!usersService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("No existe el usuario con id " + id), HttpStatus.NOT_FOUND);
        }
        /*if(usersService.existsByEmail(userDTOUpdate.getEmail()) && usersService.getUserByEmail(userDTOUpdate.getEmail()).getId() != id){
            return new ResponseEntity<>(new Mensaje("Ya existe un usuario con el email " + userDTOUpdate.getEmail()), HttpStatus.NOT_FOUND);
        }*/
        User user = usersService.getUserById(id);
        UserMapper.INSTANCIA.updateUserFromDTO(userDTOUpdate, user);

        usersService.updateUser(user);
        return new ResponseEntity<>(new Mensaje("Usuario actualizado"), HttpStatus.CREATED);
    }

}
