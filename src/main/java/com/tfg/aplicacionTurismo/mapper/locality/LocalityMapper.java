package com.tfg.aplicacionTurismo.mapper.locality;

import com.tfg.aplicacionTurismo.DTO.locality.LocalityDTO;
import com.tfg.aplicacionTurismo.entities.Locality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Clase que permite convertir un objeto en otro. En este caso, los objetos están relacionados con la entidad localidad.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Mapper
public interface LocalityMapper {

    /**
     * Instancia única de la clase LocalityMapper.
     */
    LocalityMapper INSTANCIA = Mappers.getMapper(LocalityMapper.class);

    /**
     * Método que permite convertir un objeto Locality en LocalityDTO.
     * @param locality entidad Locality.
     * @return el objeto LocalityDTO.
     */
    @Mappings({
            @Mapping(target="name", source="nameLocality")
    })
    LocalityDTO convertLocalityToLocalityDTO(Locality locality);

}
