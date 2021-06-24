package com.tfg.aplicacionTurismo.DTO;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Clase que define los atributos del objeto que se utiliza para transmitir el token.
 */
public class JwtDTO {
    private String token; //Token para el cliente

    /**
     * Constructor de la clase JwtDTO
     */
    public JwtDTO() {}

    /**
     * Constructor de la clase JwtDTO
     * @param token token
     */
    public JwtDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
