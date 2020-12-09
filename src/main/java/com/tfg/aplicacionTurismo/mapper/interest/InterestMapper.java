package com.tfg.aplicacionTurismo.mapper.interest;

import com.tfg.aplicacionTurismo.DTO.interest.NewInterestDTO;
import com.tfg.aplicacionTurismo.DTO.rol.RolDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.mapper.rol.RolMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InterestMapper {
    InterestMapper INSTANCIA = Mappers.getMapper(InterestMapper.class);
    Interest convertNewInterestInInterest (NewInterestDTO newInterestDTO);
}
