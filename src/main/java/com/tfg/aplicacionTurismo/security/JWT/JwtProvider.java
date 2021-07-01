package com.tfg.aplicacionTurismo.security.JWT;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.tfg.aplicacionTurismo.utils.Constants.*;

/**
 * Clase que genera el token y valida que este bien formado y no este expirado.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Component
public class JwtProvider {

    private static  final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    /**
     * Método que construye el token asignándole el email, los roles, fecha de creación y fecha de expiración.
     * Finalmente se firma con el algoritmo HS512 y la palabra secret.
     * @param authentication objeto Authentication que contiene la información del usuario autenticado.
     * @return token.
     * @throws UnsupportedEncodingException si el formato de codificación de caracteres no es soportado.
     */
    public String generateToken(Authentication authentication) throws UnsupportedEncodingException {

        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserDetails usuarioPrincipal = (UserDetails) authentication.getPrincipal(); //getPrincipal devuelve el UserDetails que contiene los detalles del usuario logueado
        /*
        Issue (https://stackoverflow.com/a/48203013/13331446): Para que sea un token válido en https://jwt.io/,
        es necesario que la key sea en base64 ya que se asume por defecto. Por eso es necesario convertirlo previamente
        */
        String signingKeyB64= Base64.getEncoder().encodeToString(SIGNING_KEY.getBytes("utf-8"));
        // Para generar el token se le asigna:
        return Jwts.builder().setSubject(usuarioPrincipal.getUsername()) //nombre de usuario (email)
                .claim(AUTHORITIES_KEY, authorities) //roles
                .setIssuedAt(new Date()) //fecha de creación
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000)) //fecha de expiración. El token dura 1h (3.600.000 = 1h)
                .signWith(SignatureAlgorithm.HS512, signingKeyB64) //se firma con la palabra secret
                .compact();
    }

    /**
     * Método que extrae el valor del campo subject del token. En este caso, el subject es el email del usuario.
     * @param token token.
     * @return el email del usuario.
     * @throws UnsupportedEncodingException si el formato de codificación de caracteres no es soportado.
     */
    public String getEmailFromToken(String token) throws UnsupportedEncodingException {
        String signingKeyB64= Base64.getEncoder().encodeToString(SIGNING_KEY.getBytes("utf-8"));
        return Jwts.parser().setSigningKey(signingKeyB64).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Método que valida el token.
     * @param token token.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            String signingKeyB64= Base64.getEncoder().encodeToString(SIGNING_KEY.getBytes("utf-8"));
            Jwts.parser().setSigningKey(signingKeyB64).parseClaimsJws(token); // Se valida el token, sino excepción
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
        } catch (UnsupportedEncodingException e) {
            logger.debug("Error in the encoding " +e.getMessage());
        }
        return false;
    }

    /*UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final UserDetails userDetails) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }*/


}
