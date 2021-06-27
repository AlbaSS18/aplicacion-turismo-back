package com.tfg.aplicacionTurismo.mapper.city;

import com.tfg.aplicacionTurismo.DTO.locality.LocalityDTO;
import com.tfg.aplicacionTurismo.entities.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Clase que permite convertir un objeto en otro. En este caso, los objetos están relacionados con la entidad localidad.
 */
@Mapper
public interface CityMapper {

    /**
     * Instancia única de la clase CityMapper
     */
    CityMapper INSTANCIA = Mappers.getMapper(CityMapper.class);

    /**
     * Método que permite convertir un objeto City en CityDTO
     * @param city entidad City
     * @return el objeto CityDTO
     */
    @Mappings({
            @Mapping(target="name", source="nameCity")
    })
    LocalityDTO convertCityToCityDTO (City city);

}
