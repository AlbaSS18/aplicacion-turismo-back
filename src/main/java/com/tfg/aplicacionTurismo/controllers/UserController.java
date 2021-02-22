package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTOUpdate;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.mapper.user.UserMapper;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.RelUserInterestService;
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

    @Autowired
    private InterestService interestService;

    @Autowired
    private RelUserInterestService relUserInterestService;

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        if(!usersService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("El usuario con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        User user = usersService.getUserById(id);
        UserDTO userDTO = UserMapper.INSTANCIA.convertUserToUserDTO(user);
        // Se añaden los roles a mano porque el mapper no lo hace
        Set<Rol> roles = user.getRole();
        Set<String> rolString = new HashSet<>();
        for (Rol rol: roles){
            rolString.add(rol.getRolName().name());
        }
        userDTO.setRoles(rolString);
        Set<InterestByUserDTO> interestDTOSet = new HashSet<>();
        for(RelUserInterest rel: user.getPriority()){
            InterestByUserDTO interestByUserDTO = new InterestByUserDTO();
            interestByUserDTO.setNameInterest(rel.getInterest().getNameInterest());
            interestByUserDTO.setPriority(rel.getPriority());
            interestByUserDTO.setInterestID(rel.getInterest().getId());
            interestDTOSet.add(interestByUserDTO);
        }
        userDTO.setInterest(interestDTOSet);
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
            UserDTO u = new UserDTO(user.getId(),user.getDateBirthday(),user.getEmail(),user.getUserName(), rolString);
            listUserDTO.add(u);
        }
        return new ResponseEntity<List<UserDTO>>(listUserDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if(!usersService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("El usuario con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        // NOTE: Comprobar que elimina tablas intermedias
        usersService.deleteUser(id);
        return new ResponseEntity<>(new Mensaje("Usuario eliminado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@Validated @RequestBody UserDTOUpdate userDTOUpdate, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Error en el formulario. Compruebe todos los datos"), HttpStatus.BAD_REQUEST);
        }
        if(!usersService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("No existe el usuario con id " + id), HttpStatus.NOT_FOUND);
        }
        User user = usersService.getUserById(id);
        UserMapper.INSTANCIA.updateUserFromDTO(userDTOUpdate, user);
        // Le añadimos los roles a mano porque el mapper no lo hace
        Set<String> rolesStr = userDTOUpdate.getRoles();
        Set<Rol> roles = new HashSet<>();
        for (String rol : rolesStr) {
            System.out.println(rol);
            switch (rol) {
                case "ROLE_ADMIN":
                    Rol rolAdmin = rolService.getRolByRolName(RolName.ROLE_ADMIN);
                    roles.add(rolAdmin);
                    break;
                default:
                    Rol rolUser = rolService.getRolByRolName(RolName.ROLE_USER);
                    roles.add(rolUser);
            }
        }
        user.setRole(roles);

        // Añadimos los intereses si es que vienen en el dto
        if(userDTOUpdate.getInterest() != null){
            for(InterestByUserDTO interestPrueba: userDTOUpdate.getInterest()){
                if(!interestService.existById(interestPrueba.getInterestID())){
                    return new ResponseEntity<>(new Mensaje("No existe el interest con " + interestPrueba.getInterestID()), HttpStatus.NOT_FOUND);
                }
                Interest i = interestService.getInterestById(interestPrueba.getInterestID());
                RelUserInterest rel = new RelUserInterest();
                if(relUserInterestService.existByUserAndInterest(user,i)){
                    rel = relUserInterestService.getInterestByUserAndInterest(user,i);
                }
                else{
                    rel.setUser(user);
                    rel.setInterest(i);
                }
                rel.setPriority(interestPrueba.getPriority());
                user.getPriority().add(rel);
            }
        }
        usersService.updateUser(user);
        return new ResponseEntity<>(new Mensaje("Usuario actualizado"), HttpStatus.CREATED);
    }

}
