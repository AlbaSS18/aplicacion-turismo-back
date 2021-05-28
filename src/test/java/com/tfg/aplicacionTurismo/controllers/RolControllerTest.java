package com.tfg.aplicacionTurismo.controllers;

import com.jayway.jsonpath.JsonPath;
import com.tfg.aplicacionTurismo.entities.Rol;
import com.tfg.aplicacionTurismo.entities.RolName;
import com.tfg.aplicacionTurismo.services.RolService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RolControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RolService rolService;

    List<Rol> listRoles;

    @BeforeEach
    public void setUp() {
        listRoles = new ArrayList<>();
        listRoles.add(new Rol(RolName.ROLE_ADMIN));
        listRoles.add(new Rol(RolName.ROLE_USER));
    }

    @Test
    @WithMockUser(username="alba@email.com",roles={"USER","ADMIN"}, password = "1234567")
    public void testGetRoles() throws Exception {
        // Given
        given(rolService.getRoles()).willReturn(listRoles);

        // When
        mvc.perform(
                get("/api/rol/list"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    String name1 = JsonPath.parse(json).read("$[0].rolName").toString();
                    assertThat(name1).isEqualTo("ROLE_ADMIN");

                    String name2 = JsonPath.parse(json).read("$[1].rolName").toString();
                    assertThat(name2).isEqualTo("ROLE_USER");
                });
    }
}