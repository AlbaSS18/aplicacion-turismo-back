package com.tfg.aplicacionTurismo.DTO.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Clase que define los atributos del objeto que se utiliza para autenticar a un usuario.
 */
public class LoginUserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=7)
    private String password;

    /**
     * Constructor de la clase LoginUserDTO
     */
    public LoginUserDTO() {
    }

    /**
     * Constructor de la clase LoginUserDTO
     * @param email email del usuario
     * @param password contraseña
     */
    public LoginUserDTO(@NotBlank @Email String email, @NotBlank @Size(min = 7) String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
