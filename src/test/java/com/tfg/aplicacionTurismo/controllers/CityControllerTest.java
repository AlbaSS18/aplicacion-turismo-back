package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tfg.aplicacionTurismo.DTO.city.CityDTO;
import com.tfg.aplicacionTurismo.DTO.city.NewCityDTO;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CityService cityService;

    @Autowired
    private ObjectMapper objectMapper;

    List<City> cities;

    @BeforeEach
    void setUp() {
        cities = new ArrayList<>();
        City city1 = new City("Gijon");
        city1.setId(1);
        cities.add(city1);
        City city2 = new City("Oviedo");
        city2.setId(2);
        cities.add(city2);
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldCreate() throws Exception {
        // when
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Oviedo");

        mvc.perform(post("/api/city/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldCreate_FormInvalid() throws Exception {
        // when
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName(null);

        mvc.perform(post("/api/city/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldCreate_NameCityRepeated() throws Exception {
        // given
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Oviedo");
        given(cityService.existByName(cityDTO.getName())).willReturn(true);

        // when
        mvc.perform(post("/api/city/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldDeleteCity () throws Exception {
        //given
        given(cityService.existById(5L)).willReturn(true);

        // when
        mvc.perform(delete("/api/city/delete/{id}",5L)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldDeleteCity_CityNotExist() throws Exception {
        //given
        given(cityService.existById(5L)).willReturn(false);

        // when
        mvc.perform(delete("/api/city/delete/{id}",5L)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldGetAllCities() throws Exception {
        given(cityService.getCities()).willReturn(cities);

        mvc.perform(
                get("/api/city/list"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();

                    String nameCity1 = JsonPath.parse(json).read("$[0].name").toString();
                    assertThat(nameCity1).isEqualTo("Gijon");

                    String nameCity2 = JsonPath.parse(json).read("$[1].name").toString();
                    assertThat(nameCity2).isEqualTo("Oviedo");
                });
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateCity() throws Exception {
        given(cityService.existById(1L)).willReturn(true);
        given(cityService.getCityById(1L)).willReturn(cities.get(0));

        NewCityDTO newCityDTO = new NewCityDTO();
        newCityDTO.setName("Gij");

        mvc.perform(put("/api/city/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newCityDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateCity_CityNotExist() throws Exception {
        given(cityService.existById(5L)).willReturn(false);

        NewCityDTO newCityDTO = new NewCityDTO();
        newCityDTO.setName("Gij");

        mvc.perform(put("/api/city/update/{id}", 5L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newCityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateCity_FormInvalid() throws Exception {

        NewCityDTO newCityDTO = new NewCityDTO();
        newCityDTO.setName(null);

        mvc.perform(put("/api/city/update/{id}", 5L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newCityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateCity_NameRepeated() throws Exception {

        given(cityService.existById(2L)).willReturn(true);

        NewCityDTO newCityDTO = new NewCityDTO();
        newCityDTO.setName("Gijon");

        given(cityService.existByName(newCityDTO.getName())).willReturn(true);
        given(cityService.getCityByNameCity(newCityDTO.getName())).willReturn(cities.get(0));
        System.out.println(cities.get(0).getId());
        System.out.println(cities.get(0).getNameCity());

        mvc.perform(put("/api/city/update/{id}", 2L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newCityDTO)))
                .andExpect(status().isBadRequest());
    }
}