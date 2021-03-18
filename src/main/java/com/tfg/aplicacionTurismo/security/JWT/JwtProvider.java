package com.tfg.aplicacionTurismo.security.JWT;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.tfg.aplicacionTurismo.utils.Constants.AUTHORITIES_KEY;

@Component
public class JwtProvider {

    private static  final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    final Collection<SimpleGrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {

        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserDetails usuarioPrincipal = (UserDetails) authentication.getPrincipal(); //getPrincipal devuelve el UserDetails que contiene los detalles del usuario logueado
        // Para generar el token se le asigna:
        return Jwts.builder().setSubject(usuarioPrincipal.getUsername()) //nombre de usuario (email)
                .claim(AUTHORITIES_KEY, authorities)
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
