package com.tfg.aplicacionTurismo.mapper.rol;

import com.tfg.aplicacionTurismo.DTO.rol.RolDTO;
import com.tfg.aplicacionTurismo.entities.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Clase que permite convertir un objeto en otro. En este caso, los objetos están relacionados con la entidad rol.
 */
@Mapper
public interface RolMapper {
    /**
     * Instancia única de la clase RolMapper
     */
    RolMapper INSTANCIA = Mappers.getMapper(RolMapper.class);

    /**
     * Método que permite convertir un objeto Rol en RolDTO
     * @param rol entidad Rol
     * @return el objeto RolDTO
     */
    RolDTO convertRolToRolDTO (Rol rol);
}
