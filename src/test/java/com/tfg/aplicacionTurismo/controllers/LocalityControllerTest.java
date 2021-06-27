package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tfg.aplicacionTurismo.DTO.locality.LocalityDTO;
import com.tfg.aplicacionTurismo.DTO.locality.NewLocalityDTO;
import com.tfg.aplicacionTurismo.entities.Locality;
import com.tfg.aplicacionTurismo.services.LocalityService;
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
class LocalityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    LocalityService localityService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Locality> cities;

    @BeforeEach
    void setUp() {
        cities = new ArrayList<>();
        Locality locality1 = new Locality("Gijon");
        locality1.setId(1);
        cities.add(locality1);
        Locality locality2 = new Locality("Oviedo");
        locality2.setId(2);
        cities.add(locality2);
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldCreate() throws Exception {
        // when
        LocalityDTO localityDTO = new LocalityDTO();
        localityDTO.setName("Oviedo");

        mvc.perform(post("/api/locality/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(localityDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldCreate_FormInvalid() throws Exception {
        // when
        LocalityDTO localityDTO = new LocalityDTO();
        localityDTO.setName(null);

        mvc.perform(post("/api/locality/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(localityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldCreate_NameLocalityRepeated() throws Exception {
        // given
        LocalityDTO localityDTO = new LocalityDTO();
        localityDTO.setName("Oviedo");
        given(localityService.existByName(localityDTO.getName())).willReturn(true);

        // when
        mvc.perform(post("/api/locality/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(localityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldDeleteLocality() throws Exception {
        //given
        given(localityService.existById(5L)).willReturn(true);

        // when
        mvc.perform(delete("/api/locality/delete/{id}",5L)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldDeleteLocality_LocalityNotExist() throws Exception {
        //given
        given(localityService.existById(5L)).willReturn(false);

        // when
        mvc.perform(delete("/api/locality/delete/{id}",5L)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldGetAllCities() throws Exception {
        given(localityService.getLocalities()).willReturn(cities);

        mvc.perform(
                get("/api/locality/list"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();

                    String nameLocality1 = JsonPath.parse(json).read("$[0].name").toString();
                    assertThat(nameLocality1).isEqualTo("Gijon");

                    String nameLocality2 = JsonPath.parse(json).read("$[1].name").toString();
                    assertThat(nameLocality2).isEqualTo("Oviedo");
                });
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateLocality() throws Exception {
        given(localityService.existById(1L)).willReturn(true);
        given(localityService.getLocalityById(1L)).willReturn(cities.get(0));

        NewLocalityDTO newLocalityDTO = new NewLocalityDTO();
        newLocalityDTO.setName("Gij");

        mvc.perform(put("/api/locality/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newLocalityDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateLocality_LocalityNotExist() throws Exception {
        given(localityService.existById(5L)).willReturn(false);

        NewLocalityDTO newLocalityDTO = new NewLocalityDTO();
        newLocalityDTO.setName("Gij");

        mvc.perform(put("/api/locality/update/{id}", 5L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newLocalityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateLocality_FormInvalid() throws Exception {

        NewLocalityDTO newLocalityDTO = new NewLocalityDTO();
        newLocalityDTO.setName(null);

        mvc.perform(put("/api/locality/update/{id}", 5L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newLocalityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void shouldUpdateLocality_NameRepeated() throws Exception {

        given(localityService.existById(2L)).willReturn(true);

        NewLocalityDTO newLocalityDTO = new NewLocalityDTO();
        newLocalityDTO.setName("Gijon");

        given(localityService.existByName(newLocalityDTO.getName())).willReturn(true);
        given(localityService.getLocalityByNameLocality(newLocalityDTO.getName())).willReturn(cities.get(0));
        System.out.println(cities.get(0).getId());
        System.out.println(cities.get(0).getNameLocality());

        mvc.perform(put("/api/locality/update/{id}", 2L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newLocalityDTO)))
                .andExpect(status().isBadRequest());
    }
}