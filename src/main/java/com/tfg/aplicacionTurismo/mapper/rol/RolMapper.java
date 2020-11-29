package com.tfg.aplicacionTurismo.mapper.rol;

import com.tfg.aplicacionTurismo.DTO.RolDTO;
import com.tfg.aplicacionTurismo.DTO.UserDTO;
import com.tfg.aplicacionTurismo.entities.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RolMapper {
    RolMapper INSTANCIA = Mappers.getMapper(RolMapper.class);
    RolDTO convertRolToRolDTO (Rol rol);
}
