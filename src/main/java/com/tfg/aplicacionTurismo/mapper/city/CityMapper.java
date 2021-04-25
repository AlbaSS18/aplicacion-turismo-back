package com.tfg.aplicacionTurismo.mapper.city;

import com.tfg.aplicacionTurismo.DTO.city.CityDTO;
import com.tfg.aplicacionTurismo.DTO.city.NewCityDTO;
import com.tfg.aplicacionTurismo.entities.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CityMapper {

    CityMapper INSTANCIA = Mappers.getMapper(CityMapper.class);

    @Mappings({
            @Mapping(target="name", source="nameCity")
    })
    CityDTO convertCityToCityDTO (City city);

}
