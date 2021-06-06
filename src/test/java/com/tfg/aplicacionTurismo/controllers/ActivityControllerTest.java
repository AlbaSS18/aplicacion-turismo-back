package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.aplicacionTurismo.DTO.activity.ActivityRateByUserDTO;
import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.services.ActivityService;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ActivityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ActivityService activityService;

    @MockBean
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Activity> activityList;

    @BeforeEach
    void setUp() {
        activityList = new ArrayList<>();
        Activity activity1 = new Activity("Museo del ferrocarril", "Museo de trenes", new Point(43.5409, -5.6727), "actividad1.jpg", "Plaza Estación del Nte., s/n, 33212 Gijón, Asturias");
        activity1.setCity(new City("Gijon"));
        activity1.setInterest(new Interest("Museos"));
        activityList.add(activity1);
        Activity activity2 = new Activity("Catedral de Oviedo", "Catedral de estilo gotico", new Point(43.36257, -5.84325), "actividad2.jpg", "Pl. Alfonso II el Casto, s/n, 33003 Oviedo, Asturias");
        activity2.setCity(new City("Oviedo"));
        activity2.setInterest(new Interest("Catedral"));
        activityList.add(activity2);
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER", "ADMIN"}, password = "1234567")
    public void shouldDeleteActivity() throws Exception {
        given(activityService.existsById(1L)).willReturn(true);
        given(activityService.getById(1L)).willReturn(activityList.get(0));

        mvc.perform(delete("/api/activity/delete/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER", "ADMIN"}, password = "1234567")
    public void shouldDeleteActivity_ActivityNotExist() throws Exception {
        given(activityService.existsById(1L)).willReturn(false);

        mvc.perform(delete("/api/activity/delete/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles="USER", password = "1234567")
    public void shouldRateActivity() throws Exception {

        ActivityRateByUserDTO activityRateByUserDTO = new ActivityRateByUserDTO(1L, "alba@email.com", 4);

        given(usersService.existsByEmail(activityRateByUserDTO.getEmail_user())).willReturn(true);
        given(activityService.existsById(activityRateByUserDTO.getActivity_id())).willReturn(true);

        User user = new User("alba@email.com", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"), "Alba", "1234567" );
        given(usersService.getUserByEmail(activityRateByUserDTO.getEmail_user())).willReturn(user);
        given(activityService.getById(1L)).willReturn(activityList.get(0));

        // when
        mvc.perform(post("/api/activity/rate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(activityRateByUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles="USER", password = "1234567")
    public void shouldRateActivity_FormInvalid() throws Exception {

        ActivityRateByUserDTO activityRateByUserDTO = new ActivityRateByUserDTO(1L, null, 4);

        // when
        mvc.perform(post("/api/activity/rate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(activityRateByUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles="USER", password = "1234567")
    public void shouldRateActivity_UserNotExist() throws Exception {

        ActivityRateByUserDTO activityRateByUserDTO = new ActivityRateByUserDTO(1L, "alba@email.com", 4);
        given(usersService.existsByEmail(activityRateByUserDTO.getEmail_user())).willReturn(false);

        // when
        mvc.perform(post("/api/activity/rate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(activityRateByUserDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles="USER", password = "1234567")
    public void shouldRateActivity_ActivityNotExist() throws Exception {

        ActivityRateByUserDTO activityRateByUserDTO = new ActivityRateByUserDTO(1L, "alba@email.com", 4);
        given(usersService.existsByEmail(activityRateByUserDTO.getEmail_user())).willReturn(true);
        given(activityService.existsById(activityRateByUserDTO.getActivity_id())).willReturn(false);

        // when
        mvc.perform(post("/api/activity/rate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(activityRateByUserDTO)))
                .andExpect(status().isNotFound());
    }

}