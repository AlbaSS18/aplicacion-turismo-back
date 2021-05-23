package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.aplicacionTurismo.DTO.city.CityDTO;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    private MockMvc mvc;

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private JacksonTester<List<CityDTO>> jsonCities;
    private JacksonTester<CityDTO> jsonCity;

    @BeforeEach
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(cityController).build();
    }
    @Test
    public void shouldFetchAllCities() throws Exception {
        /*// given
        List<City> cities = new ArrayList<>();
        cities.add(new City("Gijon"));
        cities.add(new City("Oviedo"));
        given(cityService.getCities()).willReturn(cities);

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/city/list").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<CityDTO> listCityDTOs = new ArrayList<>();
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Gijon");
        listCityDTOs.add(cityDTO);
        CityDTO city2DTO = new CityDTO();
        city2DTO.setName("Oviedo");
        listCityDTOs.add(city2DTO);
        assertThat(response.getContentAsString()).isEqualTo(jsonCities.write(listCityDTOs).getJson());*/
    }

    @Test
    public void shouldCreateCity() throws Exception {
        /*// when
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Oviedo");
        MockHttpServletResponse response = mvc.perform(post("/api/city/add").contentType(MediaType.APPLICATION_JSON).content(jsonCity.write(cityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());*/
    }

    @Test
    public void shouldReturnErrorWhenAddCityExist() throws Exception {
        /*// given
        List<City> cities = new ArrayList<>();
        cities.add(new City("Gijon"));
        cities.add(new City("Oviedo"));
        given(cityService.existByName("Oviedo")).willReturn(true);

        // when
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Oviedo");
        MockHttpServletResponse response = mvc.perform(post("/api/city/add").contentType(MediaType.APPLICATION_JSON).content(jsonCity.write(cityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());*/
    }

    @Test
    public void shouldReturnErrorWhenAddNotHaveName() throws Exception {
        /*// when
        CityDTO cityDTO = new CityDTO();
        MockHttpServletResponse response = mvc.perform(post("/api/city/add").contentType(MediaType.APPLICATION_JSON).content(jsonCity.write(cityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());*/
    }

    @Test
    public void shouldDeleteCity() throws Exception {
        /*// given
        given(cityService.existById((long) 1)).willReturn(true);

        // when
        MockHttpServletResponse response = mvc.perform(delete("/api/city/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());*/
    }

    @Test
    public void shouldReturnErrorWhenDeleteCityNotExist() throws Exception {
        /*// when
        MockHttpServletResponse response = mvc.perform(delete("/api/city/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());*/
    }

}