package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.*;
import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import com.tfg.aplicacionTurismo.DTO.user.LoginUserDTO;
import com.tfg.aplicacionTurismo.DTO.user.NewUserDTO;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.security.JWT.JwtProvider;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.RolService;
import com.tfg.aplicacionTurismo.services.UsersService;
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

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que responde a las acciones relacionadas con la autenticación y el registro.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Método que registra a un usuario en la aplicación web.
     * @param userDTO objeto DTO que contiene la información necesaria para registrar a un usuario.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return respuesta HTTP que contiene un mensaje indicando que el usuario se ha registrado con éxito
     * o la respuesta HTTP que contiene un mensaje de error si los datos no son correctos, si ya existe un usuario utilizando ese email
     * o si las contraseñas no son iguales.
     */
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
        User user = new User(userDTO.getEmail(), userDTO.getDateBirthday(), userDTO.getUserName(), userDTO.getPassword());

        // Le añadimos los intereses
        Set<InterestDTO> interestStr = userDTO.getInterest();
        RelUserInterest rel;
        for (InterestDTO interest : interestStr) {
            rel = new RelUserInterest();
            rel.setPriority(interest.getPriority());
            Interest i = interestService.getInterestByName(interest.getNameInterest());
            rel.setInterest(i);
            rel.setUser(user);
            user.getPriority().add(rel);
            i.getPriority().add(rel);
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
        return new ResponseEntity(new Mensaje("Usuario añadido"), HttpStatus.CREATED);
    }

    /**
     * Método que autentica a un usuario en la aplicación web.
     * @param loginDTO objeto DTO que contiene la información necesaria para autenticar a un usuario en la aplicación web.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return la respuesta HTTP que contiene el token.
     * @throws UnsupportedEncodingException si el formato de codificación de caracteres no es soportado.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginUserDTO loginDTO, BindingResult result) throws UnsupportedEncodingException {
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDTO jwtDTO = new JwtDTO(jwt);

        System.out.println(jwtDTO.getToken());

        return new ResponseEntity<JwtDTO>(jwtDTO, HttpStatus.OK);
    }
}
