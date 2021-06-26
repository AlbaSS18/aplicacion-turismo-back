package com.tfg.aplicacionTurismo.security.JWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Clase que rechaza una petición con unas credenciales incorrectas.
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private static  final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    /**
     * Método que es invocado cuando un usuario solicita un recurso HTTP sin estar autenticado. Envía el error 401.
     * @param req objeto HttpServletRequest que permite obtener información sobre la petición HTTP de los clientes.
     * @param res objeto HttpServletResponse que se emplea para enviar el resultado de procesar una petición HTTP a un cliente.
     * @param e objeto AuthenticationException que causa la invocación al método.
     * @throws IOException si hay un fallo en el método sendError
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException {
        logger.debug("Checking login is failing");
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "wrong credentials");
    }
}
