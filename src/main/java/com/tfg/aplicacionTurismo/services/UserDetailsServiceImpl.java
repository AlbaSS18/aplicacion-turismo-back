package com.tfg.aplicacionTurismo.services;

import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que comprueba si las credenciales de autenticación son correctas.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository usersRepository;

    /**
     * Método que valida si el usuario existe en la base de datos y creará un objeto UserDetails.
     * @param email correo electrónico del usuario.
     * @return objeto UserDetails
     * @throws UsernameNotFoundException si las credenciales no son correctas.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Bad credentials");
        }
        Set<GrantedAuthority> grantedAuthorities = user.getRole().stream().map(rol -> new SimpleGrantedAuthority(rol.getRolName().name())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
