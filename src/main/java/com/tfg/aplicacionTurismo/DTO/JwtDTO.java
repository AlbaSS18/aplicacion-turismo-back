package com.tfg.aplicacionTurismo.DTO;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDTO {
    private String token; //Token para el cliente

    public JwtDTO() {}

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
