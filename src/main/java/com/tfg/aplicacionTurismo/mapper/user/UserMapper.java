package com.tfg.aplicacionTurismo.mapper.user;

import com.tfg.aplicacionTurismo.DTO.user.UserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTOUpdate;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.mapper.rol.RolMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Clase que permite convertir un objeto en otro. En este caso, los objetos están relacionados con la entidad usuario.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Mapper
public interface UserMapper {

    /**
     * Instancia única de la clase UserMapper.
     */
    UserMapper INSTANCIA= Mappers.getMapper(UserMapper.class);

    /**
     * Método que permite convertir un objeto UserDTOUpdate en User.
     * @param dto objeto UserDTOUpdate.
     * @param userEntity objeto User.
     */
    void updateUserFromDTO (UserDTOUpdate dto, @MappingTarget User userEntity);

    /**
     * Método que permite convertir un objeto User en UserDTO.
     * @param user entidad User.
     * @return el objeto UserDTO.
     */
    UserDTO convertUserToUserDTO (User user);
}
