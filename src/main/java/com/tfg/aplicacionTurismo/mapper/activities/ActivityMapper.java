package com.tfg.aplicacionTurismo.mapper.activities;

import com.tfg.aplicacionTurismo.DTO.activity.ActivitySendDTO;
import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityMapper {

    ActivityMapper INSTANCIA = Mappers.getMapper(ActivityMapper.class);
}
