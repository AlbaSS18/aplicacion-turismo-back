package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.CityDTO;
import com.tfg.aplicacionTurismo.entities.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void shouldFetchAllActivities() throws Exception {

    }
}