package com.tfg.aplicacionTurismo.entities;

import java.io.Serializable;

/**
 * Clase que define una llave primaria compuesta.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class UserInterestId implements Serializable {

    private long user;
    private long interest;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getInterest() {
        return interest;
    }

    public void setInterest(long interest) {
        this.interest = interest;
    }
}
