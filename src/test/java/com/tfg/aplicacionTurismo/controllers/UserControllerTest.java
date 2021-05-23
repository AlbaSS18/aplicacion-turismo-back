package com.tfg.aplicacionTurismo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.aplicacionTurismo.DTO.user.UserDTO;
import com.tfg.aplicacionTurismo.services.RolService;
import com.tfg.aplicacionTurismo.services.UsersService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mvc;

    @Mock
    private UsersService usersService;

    @Mock
    private RolService rolService;

    @InjectMocks
    private UserController userController;

    private JacksonTester<List<UserDTO>> jsonUsers;
    private JacksonTester<UserDTO> jsonUser;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldDetailsUser() throws Exception {
        /*// given
        Set<Rol> rolEntity = new HashSet<>();
        Rol rol1 = new Rol();
        rol1.setRolName(RolName.ROLE_USER);
        //given(rolService.getRolByRolName(RolName.ROLE_USER)).willReturn(rol1);
        rolEntity.add(rol1);
        User user1 = new User("alba@gmail.com", 21, "Mujer", "Alba", "1234567" );
        user1.setRole(rolEntity);
        System.out.println(user1.getRole());
        for (Rol r: user1.getRole()){
            System.out.println(r.getRolName());
        }
        given(rolService.getRolByRolName(RolName.ROLE_USER)).willReturn(rol1);
        given(usersService.existsById(new Long(1))).willReturn(true);
        given(usersService.getUserById(new Long(1))).willReturn(user1);

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/user/details/1").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        UserDTO userDTO = new UserDTO(new Long(0),21, "alba@gmail.com", "Mujer", "Alba", roles);
        for (String r: userDTO.getRoles()){
            System.out.println(r);
        }
        System.out.println(userDTO.getRoles());
        assertThat(response.getContentAsString()).isEqualTo(jsonUser.write(userDTO).getJson());*/
    }

    @Test
    public void shouldReturnExceptionWhenDetailUserNotExist() throws Exception {
        /*// when
        MockHttpServletResponse response = mvc.perform(get("/api/user/details/1").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());*/
    }
}