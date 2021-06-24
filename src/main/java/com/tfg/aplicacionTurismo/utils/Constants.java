package com.tfg.aplicacionTurismo.utils;

/**
 * Clase que contiene constantes que serán utilizadas para crear el token de autenticación.
 */
public class Constants {

    /**
     * Nombre del claim que contiene los roles del usuario en el token.
     */
    public static final String AUTHORITIES_KEY = "ROLES";
    /**
     * Palabra clave con la que será firmada el token.
     */
    public static final String SIGNING_KEY = "SECRET";
    /**
     * Tiempo de caducidad del token.
     */
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 3600;
    /**
     * Cabecera donde se guarda el token.
     */
    public static final String HEADER_STRING = "Authorization";
    /**
     * Prefijo que acompaña al token.
     */
    public static final String TOKEN_PREFIX = "Bearer ";
}
