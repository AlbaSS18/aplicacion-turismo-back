package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.aplicacionTurismo.DTO.interest.InterestDTO;
import com.tfg.aplicacionTurismo.DTO.user.LoginUserDTO;
import com.tfg.aplicacionTurismo.DTO.user.NewUserDTO;
import com.tfg.aplicacionTurismo.entities.Interest;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.RolName;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.RolService;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @MockBean
    private RolService rolService;

    @MockBean
    private InterestService interestService;

    Set<InterestDTO> interest;
    Set<String> roles;

    @BeforeEach
    void setUp() {
        interest = new HashSet<>();
        interest.add(new InterestDTO("Museos", 10));
        interest.add(new InterestDTO("Catedrales", 6));

        roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
    }

    @Test
    public void testCreateUser() throws Exception {

        // given
        given(rolService.getRolByRolName(RolName.ROLE_USER)).willReturn(new Rol(RolName.ROLE_USER));
        given(rolService.getRolByRolName(RolName.ROLE_ADMIN)).willReturn(new Rol(RolName.ROLE_ADMIN));

        NewUserDTO newUserDTO = new NewUserDTO("alba@email.com", "Alba", "1234567", "1234567", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"),
                roles, interest);

        for(InterestDTO interestDTO: interest){
            given(interestService.getInterestByName(interestDTO.getNameInterest())).willReturn(new Interest(interestDTO.getNameInterest()));
        }

        // when
        mvc.perform(post("/api/auth/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(newUserDTO)))
            .andExpect(status().is(201));
    }

    @Test
    public void testCreateUser_EmailRepeated() throws Exception{

        // given
        given(usersService.existsByEmail("alba@email.com")).willReturn(true);

        NewUserDTO newUserDTO = new NewUserDTO("alba@email.com", "Alba", "1234567", "1234567", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"),
                roles, interest);


        // when
        mvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().is(400));
    }

    @Test
    public void testCreateUser_PasswordNotSame() throws Exception{

        // given
        NewUserDTO newUserDTO = new NewUserDTO("alba@email.com", "Alba", "12345678910", "1234567", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"),
                roles, interest);

        // when
        mvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().is(400));
    }

    @Test
    public void testCreateUser_FormInvalid() throws Exception{

        // given
        NewUserDTO newUserDTO = new NewUserDTO("alba@email.com", null, "12345678910", "1234567", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"),
                roles, interest);

        // when
        mvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().is(400));
    }

}