package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Clase que representa el tipo de rol web.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RolName rolName;

    /**
     * Constructor de la clase Rol.
     */
    public Rol() {
    }

    /**
     * Constructor de la clase Rol.
     * @param rolName nombre del rol.
     */
    public Rol(@NotNull RolName rolName) {
        this.rolName = rolName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RolName getRolName() {
        return rolName;
    }

    public void setRolName(RolName rolName) {
        this.rolName = rolName;
    }
}
