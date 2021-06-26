package com.tfg.aplicacionTurismo.security.JWT;

import com.tfg.aplicacionTurismo.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.tfg.aplicacionTurismo.utils.Constants.HEADER_STRING;
import static com.tfg.aplicacionTurismo.utils.Constants.TOKEN_PREFIX;

/**
 * Clase que filtra las peticiones que reciba el servidor para determinar si poseen token o no.
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private static  final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * Método que comprueba si la petición tiene un token válido.
     * Este metodo se invoca cada vez que se le haga una peticion al servidor.
     * @param req objeto HttpServletRequest que permite obtener información sobre la petición HTTP de los clientes.
     * @param res objeto HttpServletResponse que se emplea para enviar el resultado de procesar una petición HTTP a un cliente.
     * @param filterChain objeto FilterChain que se utiliza para invocar al siguiente filtro o recurso de la cadena.
     * @throws ServletException si hay un fallo en el método doFilter
     * @throws IOException si hay un fallo en el método doFilter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(req);
            // El token tiene forma de "Bearer xxxxx.yyyyy.zzzz". Con este método, eliminas el Bearer
            if(token !=null && jwtProvider.validateToken(token)){
                String userName = jwtProvider.getEmailFromToken(token);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception e){
            logger.debug("Authentication is not correct " + e.getMessage());
        }
        filterChain.doFilter(req, res);
    }

    /**
     * Método que elimina el prefijo y obtiene el token.
     * @param request objeto HttpServletRequest que permite obtener información sobre la petición HTTP de los clientes.
     * @return token
     */
    private String getToken(HttpServletRequest request){
        String authReq = request.getHeader(HEADER_STRING);
        if(authReq != null && authReq.startsWith(TOKEN_PREFIX))
            return authReq.replace(TOKEN_PREFIX, "");
        return null;
    }
}
