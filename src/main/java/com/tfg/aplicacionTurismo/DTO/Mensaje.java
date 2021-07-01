package com.tfg.aplicacionTurismo.DTO;

/**
 * Clase que define los atributos del objeto que se utiliza para transmitir mensajes.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
public class Mensaje {
    private String mensaje;

    /**
     * Contructor de la clase Mensaje.
     * @param mensaje mensaje.
     */
    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
