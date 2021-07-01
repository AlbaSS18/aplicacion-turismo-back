package com.tfg.aplicacionTurismo.mapper.interest;

import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import com.tfg.aplicacionTurismo.DTO.interest.InterestListDTO;
import com.tfg.aplicacionTurismo.DTO.interest.NewInterestDTO;
import com.tfg.aplicacionTurismo.DTO.rol.RolDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.mapper.rol.RolMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Clase que permite convertir un objeto en otro. En este caso, los objetos están relacionados con la entidad interés.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Mapper
public interface InterestMapper {
    /**
     * Instancia única de la clase InterestMapper.
     */
    InterestMapper INSTANCIA = Mappers.getMapper(InterestMapper.class);

    /**
     * Método que permite convertir un objeto NewInterestDTO en Interest.
     * @param newInterestDTO objeto NewInterestDTO.
     * @return la entidad Interest.
     */
    Interest convertNewInterestToInterest (NewInterestDTO newInterestDTO);

    /**
     * Método que permite convertir un objeto Interest en InterestListDTO.
     * @param interest entidad Interest.
     * @return el objeto InterestListDTO.
     */
    InterestListDTO convertInterestToInterestListDTO (Interest interest);
}
