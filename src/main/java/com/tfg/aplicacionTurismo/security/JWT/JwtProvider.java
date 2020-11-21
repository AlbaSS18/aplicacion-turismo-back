package com.tfg.aplicacionTurismo.security.JWT;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private static  final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        UserDetails usuarioPrincipal = (UserDetails) authentication.getPrincipal(); //getPrincipal devuelve el UserDetails que contiene los detalles del usuario logueado
        // Para generar el token se le asigna:
        return Jwts.builder().setSubject(usuarioPrincipal.getUsername()) //nombre de usuario (email)
                .setIssuedAt(new Date()) //fecha de creación
                .setExpiration(new Date(new Date().getTime() + expiration * 1000)) //fecha de expiración
                .signWith(SignatureAlgorithm.HS512, secret) //se firma
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token); // Se valida el token, sino excepción
            return true;
        } catch (MalformedJwtException e) {
            logger.debug("Badly formed token " +e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.debug("Token not supported " +e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.debug("Expired token " +e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.debug("Empty token " +e.getMessage());
        } catch (SignatureException e) {
            logger.debug("Error in the signature " +e.getMessage());
        }
        return false;
    }


}
