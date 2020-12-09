package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.aplicacionTurismo.DTO.activity.ActivityDTO;
import com.tfg.aplicacionTurismo.entities.Activity;
import com.tfg.aplicacionTurismo.entities.City;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.services.ActivityService;
import com.tfg.aplicacionTurismo.services.CityService;
import com.tfg.aplicacionTurismo.services.InterestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.geo.Point;
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
class ActivityControllerTest {

    private MockMvc mvc;

    @Mock
    private ActivityService activityService;

    @Mock
    private CityService cityService;

    @Mock
    private InterestService interestService;

    @InjectMocks
    private ActivityController activityController;

    private JacksonTester<List<ActivityDTO>> jsonActivities;
    private JacksonTester<ActivityDTO> jsonActivity;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(activityController).build();
    }

    @Test
    public void shouldFetchAllActivities() throws Exception {
        // given
        List<Activity> activities = new ArrayList<>();
        Activity activity1 = new Activity("Museo del ferrocarril", "Museo de trenes", new Point(43.5409, -5.6727), "src/app/img/museodelferrocarril.jpg");
        activity1.setCity(new City("Gijon"));
        activity1.setInterest(new Interest("Museos"));
        activities.add(activity1);
        Activity activity2 = new Activity("Catedral de Oviedo", "Catedral de estilo gotico", new Point(43.36257, -5.84325), "src/app/img/catedralOviedo.jpg");
        activity2.setCity(new City("Oviedo"));
        activity2.setInterest(new Interest("Catedral"));
        activities.add(activity2);
        given(activityService.getActivities()).willReturn(activities);

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/activity/list").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<ActivityDTO> listActivitiesDTOs = new ArrayList<>();
        listActivitiesDTOs.add(new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos"));
        listActivitiesDTOs.add(new ActivityDTO("Catedral de Oviedo","Catedral de estilo gotico", 43.36257, -5.84325, "Oviedo", "Catedral"));
        assertThat(response.getContentAsString()).isEqualTo(jsonActivities.write(listActivitiesDTOs).getJson());
    }

    @Test
    public void shouldDeleteActivity() throws Exception {
        // given
        given(activityService.existsById((long) 1)).willReturn(true);

        // when
        MockHttpServletResponse response = mvc.perform(delete("/api/activity/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldReturnErrorWhenDeleteActivityNotExist() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(delete("/api/activity/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldReturnErrorWhenExistsActivitySameName() throws Exception {
        // get
        given(cityService.existByName("Gijon")).willReturn(true);
        given(interestService.existByName("Museos")).willReturn(true);
        given(activityService.existsByName("Museo del ferrocarril")).willReturn(true);

        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnErrorWhenNotExistInterest() throws Exception {
        // get
        given(cityService.existByName("Gijon")).willReturn(true);

        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldReturnErrorWhenNotExistCity() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldAddActivity() throws Exception {
        // get
        given(cityService.existByName("Gijon")).willReturn(true);
        given(interestService.existByName("Museos")).willReturn(true);

        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityNameIsLongCero() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO("","Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityDescriptionIsLongCero() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityCityIsLongCero() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityInterestIsLongCero() throws Exception {
        // get
        given(cityService.existByName("Gijon")).willReturn(true);

        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo del ferrocarril","Museo de trenes", 43.5409, -5.6727, "Gijon", "");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityNameIsNull() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO("Museo de trenes", 43.5409, -5.6727, "Gijon", "Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityDescriptionIsNull() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName("Museo del ferrocarril");
        activityDTO.setLatitude(43.5409);
        activityDTO.setLongitude(-5.6727);
        activityDTO.setCity("Gijon");
        activityDTO.setInterest("Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityCityIsNull() throws Exception {
        // when
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName("Museo del ferrocarril");
        activityDTO.setDescription("Museo de trenes");
        activityDTO.setLatitude(43.5409);
        activityDTO.setLongitude(-5.6727);
        activityDTO.setInterest("Museos");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturnExceptionWhenAddActivityInterestIsNull() throws Exception {
        // get
        given(cityService.existByName("Gijon")).willReturn(true);

        // when
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName("Museo del ferrocarril");
        activityDTO.setDescription("Museo de trenes");
        activityDTO.setLatitude(43.5409);
        activityDTO.setLongitude(-5.6727);
        activityDTO.setCity("Gijon");
        MockHttpServletResponse response = mvc.perform(post("/api/activity/add").contentType(MediaType.APPLICATION_JSON).content(jsonActivity.write(activityDTO).getJson())).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



}