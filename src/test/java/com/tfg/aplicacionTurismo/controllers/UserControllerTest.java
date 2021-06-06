package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tfg.aplicacionTurismo.DTO.interest.InterestByUserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTO;
import com.tfg.aplicacionTurismo.DTO.user.UserDTOUpdate;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.services.InterestService;
import com.tfg.aplicacionTurismo.services.RelUserInterestService;
import com.tfg.aplicacionTurismo.services.RolService;
import com.tfg.aplicacionTurismo.services.UsersService;
import org.apache.catalina.util.ResourceSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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

    @MockBean
    private RelUserInterestService relUserInterestService;

    User user;

    @BeforeEach
    void setUp() throws ParseException {
        user = new User("alba@gmail.com", new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"), "Alba", "1234567" );

        Set<Rol> rolEntity = new HashSet<>();
        Rol rol1 = new Rol(RolName.ROLE_USER);
        rolEntity.add(rol1);
        user.setRole(rolEntity);

        Set<RelUserInterest> relUserInterests = new ResourceSet<>();
        RelUserInterest relUserInterest = new RelUserInterest();
        relUserInterest.setInterest(new Interest("Museos"));
        relUserInterest.setUser(user);
        relUserInterest.setPriority(5);
        relUserInterests.add(relUserInterest);
        user.setPriority(relUserInterests);
    }

    @Test
    @WithMockUser(username="alba@email.com", password = "1234567")
    public void shouldDetailsUser() throws Exception {

        // given
        given(usersService.existsById(1L)).willReturn(true);
        given(usersService.getUserById(1L)).willReturn(user);

        // when
        mvc.perform(get("/api/user/details/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();

                    String nameUser = JsonPath.parse(json).read("userName").toString();
                    assertThat(nameUser).isEqualTo("Alba");

                    String email = JsonPath.parse(json).read("email").toString();
                    assertThat(email).isEqualTo("alba@gmail.com");

                    String dateBirthday = JsonPath.parse(json).read("dateBirthday").toString();
                    assertThat(dateBirthday).isEqualTo("1998-12-17T23:00:00.000+00:00");

                    String roles = JsonPath.parse(json).read("roles[0]").toString();
                    assertThat(roles).isEqualTo("ROLE_USER");

                    String interestName = JsonPath.parse(json).read("interest[0].nameInterest").toString();
                    assertThat(interestName).isEqualTo("Museos");
                });
    }

    @Test
    @WithMockUser(username="alba@email.com", password = "1234567")
    public void shouldDetailsUser_UserNotExist() throws Exception {
        // when
        mvc.perform(get("/api/user/details/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="alba@email.com", password = "1234567")
    public void shouldGetUsers() throws Exception {
        // given
        List<User> userList = new ArrayList<>();
        userList.add(user);

        given(usersService.getUsers()).willReturn(userList);

        // when
        mvc.perform(get("/api/user/list"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();

                    String nameUser = JsonPath.parse(json).read("$[0].userName").toString();
                    assertThat(nameUser).isEqualTo("Alba");

                    String email = JsonPath.parse(json).read("$[0].email").toString();
                    assertThat(email).isEqualTo("alba@gmail.com");

                    String dateBirthday = JsonPath.parse(json).read("$[0].dateBirthday").toString();
                    assertThat(dateBirthday).isEqualTo("1998-12-17T23:00:00.000+00:00");

                    String roles = JsonPath.parse(json).read("$[0].roles[0]").toString();
                    assertThat(roles).isEqualTo("ROLE_USER");
                });
    }

    @Test
    @WithMockUser(username="alba@email.com", roles={"USER","ADMIN"}, password = "1234567")
    public void shouldDeleteUser() throws Exception {
        // given
        given(usersService.existsById(1L)).willReturn(true);
        // when
        mvc.perform(delete("/api/user/delete/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alba@email.com", roles={"USER","ADMIN"}, password = "1234567")
    public void shouldDeleteUser_UserNotExist() throws Exception {
        // given
        given(usersService.existsById(1L)).willReturn(false);
        // when
        mvc.perform(delete("/api/user/delete/{id}", 1L))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username="alba@email.com", password = "1234567")
    public void shouldUpdateUser() throws Exception {

        // given
        given(usersService.getUserById(1L)).willReturn(user);

        given(usersService.existsById(1L)).willReturn(true);
        Set<String> rolEntity = new HashSet<>();
        String rol1 = RolName.ROLE_USER.toString();
        rolEntity.add(rol1);
        String rol2 = RolName.ROLE_ADMIN.toString();
        rolEntity.add(rol2);

        Set<InterestByUserDTO> interestByUser = new HashSet<>();

        InterestByUserDTO interestMuseos = new InterestByUserDTO(1L,"Museos", 5);
        interestByUser.add(interestMuseos);
        Interest interest1 = new Interest(interestMuseos.getNameInterest());
        given(interestService.existById(1L)).willReturn(true);
        given(interestService.getInterestById(interestMuseos.getInterestID())).willReturn(interest1);
        given(relUserInterestService.existByUserAndInterest(user,interest1)).willReturn(false);

        InterestByUserDTO interestCatedrales = new InterestByUserDTO(2L,"Catedrales", 5);
        interestByUser.add(interestCatedrales);
        Interest interest2 = new Interest(interestCatedrales.getNameInterest());
        given(interestService.existById(2L)).willReturn(true);
        given(interestService.getInterestById(interestMuseos.getInterestID())).willReturn(interest2);
        given(relUserInterestService.existByUserAndInterest(user,interest2)).willReturn(true);
        given(relUserInterestService.getInterestByUserAndInterest(user,interest2)).willReturn(new RelUserInterest());


        UserDTOUpdate userDTOUpdate = new UserDTOUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"), "albita",rolEntity, interestByUser);

        // when
        mvc.perform(put("/api/user/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDTOUpdate)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="alba@email.com", password = "1234567")
    public void shouldUpdateUser_FormInvalid() throws Exception {

        Set<String> rolEntity = new HashSet<>();
        String rol1 = RolName.ROLE_USER.toString();
        rolEntity.add(rol1);

        Set<InterestByUserDTO> interestByUser = new HashSet<>();
        InterestByUserDTO interestByUserDTO = new InterestByUserDTO(1L,"Museos", 5);
        interestByUser.add(interestByUserDTO);

        UserDTOUpdate userDTOUpdate = new UserDTOUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"), null,rolEntity, interestByUser);

        // when
        mvc.perform(put("/api/user/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDTOUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="alba@email.com", password = "1234567")
    public void shouldUpdateUser_UserNotExist() throws Exception {
        // given
        given(usersService.existsById(1L)).willReturn(false);

        Set<String> rolEntity = new HashSet<>();
        String rol1 = RolName.ROLE_USER.toString();
        rolEntity.add(rol1);

        Set<InterestByUserDTO> interestByUser = new HashSet<>();
        InterestByUserDTO interestByUserDTO = new InterestByUserDTO(1L,"Museos", 5);
        interestByUser.add(interestByUserDTO);

        UserDTOUpdate userDTOUpdate = new UserDTOUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("18-12-1998"), "albita",rolEntity, interestByUser);

        // when
        mvc.perform(put("/api/user/update/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDTOUpdate)))
                .andExpect(status().isNotFound());
    }
}