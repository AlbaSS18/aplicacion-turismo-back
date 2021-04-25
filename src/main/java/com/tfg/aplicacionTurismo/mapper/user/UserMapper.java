package com.tfg.aplicacionTurismo.mapper.user;

import com.tfg.aplicacionTurismo.DTO.user.UserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTOUpdate;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.mapper.rol.RolMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCIA= Mappers.getMapper(UserMapper.class);

    void updateUserFromDTO (UserDTOUpdate dto, @MappingTarget User userEntity);

    UserDTO convertUserToUserDTO (User user);
}
