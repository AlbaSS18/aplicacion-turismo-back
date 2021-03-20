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

public class JwtTokenFilter extends OncePerRequestFilter {

    private static  final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(req);
            // El token tiene forma de "Bearer xxxxx.yyyyy.zzzz". Con este método, eliminas el Bearer
            if(token !=null && jwtProvider.validateToken(token)){
                String userName = jwtProvider.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception e){
            logger.debug("Authentication is not correct " + e.getMessage());
        }
        filterChain.doFilter(req, res);
    }

    private String getToken(HttpServletRequest request){
        String authReq = request.getHeader(HEADER_STRING);
        if(authReq != null && authReq.startsWith(TOKEN_PREFIX))
            return authReq.replace(TOKEN_PREFIX, "");
        return null;
    }
}
