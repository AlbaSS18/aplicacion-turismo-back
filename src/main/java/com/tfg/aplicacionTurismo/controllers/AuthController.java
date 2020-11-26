package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.*;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.security.JWT.JwtEntryPoint;
import com.tfg.aplicacionTurismo.security.JWT.JwtProvider;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.RolService;
import com.tfg.aplicacionTurismo.services.SecurityService;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private RolService rolService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Validated @RequestBody NewUserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if (usersService.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity(new Mensaje("Ya existe ese email"), HttpStatus.BAD_REQUEST);
        }
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm())) {
            return new ResponseEntity(new Mensaje("Contraseñas no son iguales"), HttpStatus.BAD_REQUEST);
        }
        User user = new User(userDTO.getEmail(), userDTO.getAge(), userDTO.getGenre(), userDTO.getUserName(), userDTO.getPassword());
        // Le añadimos los intereses
        Set<InterestDTO> interestStr = userDTO.getInterest();
        RelUserInterest rel;
        for (InterestDTO interest : interestStr) {
            rel = new RelUserInterest();
            rel.setPriority(interest.getPriority());
            System.out.println(interest.getNameInterest());
            Interest i = interestService.getInterestByName(interest.getNameInterest());
            rel.setInterest(i);
            rel.setUser(user);
            user.getPriority().add(rel);
            i.getPriority().add(rel);
            System.out.println("uer" + rel.getUser().getId());
            System.out.println("interest" + rel.getInterest().getId());
        }

        // Le añadimos los roles
        Set<String> rolesStr = userDTO.getRoles();
        Set<Rol> roles = new HashSet<>();
        for (String rol : rolesStr) {
            switch (rol) {
                case "admin":
                    Rol rolAdmin = rolService.getRolByRolName(RolName.ROLE_ADMIN);
                    roles.add(rolAdmin);
                    break;
                default:
                    Rol rolUser = rolService.getRolByRolName(RolName.ROLE_USER);
                    roles.add(rolUser);
            }
        }
        user.setRole(roles);
        usersService.addUser(user);
        securityService.autoLogin(userDTO.getEmail(), userDTO.getPassword());
        for (RelUserInterest reli : user.getPriority()) {
            System.out.println(reli.getId());
            System.out.println(reli.getInterest().getNameInterest());
            System.out.println(reli.getUser().getEmail());
            System.out.println(reli.getPriority());
        }
        return new ResponseEntity(new Mensaje("Usuario añadido"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginUserDTO loginDTO, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        //logger.error(String.valueOf(loginDTO));
        //System.out.println(loginDTO.getEmail() + loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        //System.out.println(jwt);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //System.out.println(userDetails.getUsername());
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<JwtDTO>(jwtDTO, HttpStatus.OK);
    }
}
