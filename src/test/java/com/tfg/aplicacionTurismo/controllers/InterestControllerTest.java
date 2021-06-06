package com.tfg.aplicacionTurismo.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tfg.aplicacionTurismo.DTO.interest.NewInterestDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.User;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.UsersService;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InterestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InterestService interestService;

    @MockBean
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Interest> interestList;

    @BeforeEach
    public void setUp() {
        interestList = new ArrayList<>();
        Interest interest1 = new Interest();
        interest1.setId(1);
        interest1.setNameInterest("Museos");
        interestList.add(interest1);

        Interest interest2 = new Interest();
        interest2.setId(2);
        interest2.setNameInterest("Catedrales");
        interestList.add(interest2);
    }

    @Test
    public void testGetInterest() throws Exception {
        // Given
        given(interestService.getInterests()).willReturn(interestList);

        // When
        mvc.perform(
                get("/api/interest/list"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();

                    String name1 = JsonPath.parse(json).read("$[0].nameInterest").toString();
                    assertThat(name1).isEqualTo("Museos");

                    String name2 = JsonPath.parse(json).read("$[1].nameInterest").toString();
                    assertThat(name2).isEqualTo("Catedrales");
                });
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testAddInterest() throws Exception {
        // given
        List<User> userList = new ArrayList<>();
        userList.add(new User("alba@email.com", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"), "Alba", "1234567"));
        given(usersService.getUsers()).willReturn(userList);
        NewInterestDTO newInterestDTO = new NewInterestDTO("Museos");

        // when
        mvc.perform(post("/api/interest/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newInterestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testAddInterest_NameInterestRepeated() throws Exception {
        // given
        NewInterestDTO newInterestDTO = new NewInterestDTO("Museos");
        given(interestService.existByName(newInterestDTO.getNameInterest())).willReturn(true);

        // when
        mvc.perform(post("/api/interest/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newInterestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testAddInterest_FormInvalid() throws Exception {
        // given
        NewInterestDTO newInterestDTO = new NewInterestDTO(null);

        // when
        mvc.perform(post("/api/interest/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newInterestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testDeleteInterest() throws Exception {
        //given
        given(interestService.existById(1L)).willReturn(true);

        // when
        mvc.perform(delete("/api/interest/delete/{id}",1L)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testDeleteInterest_InterestNotExist() throws Exception {
        //given
        given(interestService.existById(7L)).willReturn(false);

        // when
        mvc.perform(delete("/api/interest/delete/{id}",7L)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testUpdateInterest() throws Exception {
        //given
        given(interestService.existById(1L)).willReturn(true);
        given(interestService.getInterestById(1L)).willReturn(interestList.get(0));

        // when
        NewInterestDTO auxCopyInterest = new NewInterestDTO("Museos1");

        mvc.perform(put("/api/interest/update/{id}",1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(auxCopyInterest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testUpdateInterest_InterestNotExist() throws Exception {
        // when
        NewInterestDTO auxCopyInterest = new NewInterestDTO("Museos1");

        mvc.perform(put("/api/interest/update/{id}",7L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(auxCopyInterest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testUpdateInterest_FormInvalid() throws Exception {
        // given
        NewInterestDTO auxCopyInterest = new NewInterestDTO(null);

        // when
        mvc.perform(put("/api/interest/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(auxCopyInterest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testUpdateInterest_NameRepeated() throws Exception {
        // given
        NewInterestDTO auxCopyInterest = new NewInterestDTO("Catedrales");
        given(interestService.existById(1L)).willReturn(true);
        given(interestService.existByName(auxCopyInterest.getNameInterest())).willReturn(true);
        given(interestService.getInterestByName(auxCopyInterest.getNameInterest())).willReturn(interestList.get(1));


        // when
        mvc.perform(put("/api/interest/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(auxCopyInterest)))
                .andExpect(status().isBadRequest());
    }
}