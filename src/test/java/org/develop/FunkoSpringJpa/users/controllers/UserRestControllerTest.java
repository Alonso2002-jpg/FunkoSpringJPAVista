package org.develop.FunkoSpringJpa.users.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.FunkoSpringJpa.pages.models.PageResponse;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserInfoResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserRequestDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.develop.FunkoSpringJpa.rest.users.exceptions.UserNotFoundException;
import org.develop.FunkoSpringJpa.rest.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "password", roles = {"ADMIN", "USER"})
class UserRestControllerTest {

    private final UserRequestDto userRequestDto = UserRequestDto.builder()
            .name("test")
            .email("test@example.com")
            .username("test")
            .password("test1234")
            .build();
    private final User user = User.builder()
            .id(99L)
            .name("test")
            .username("test")
            .password("test1234")
            .email("test@example.com")
            .build();
    private final UserResponseDto responseDto = UserResponseDto.builder()
            .id(99L)
            .name("test")
            .username("test")
            .password("test1234")
            .email("test@example.com")
            .build();
    private final UserInfoResponseDto infoResponseDto = UserInfoResponseDto.builder()
            .id(99L)
            .name("test")
            .username("test")
            .email("test@example.com")
            .build();
    private final String myEndPoint = "/v1/users";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    public UserRestControllerTest(UserService userService) {
        this.userService = userService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithAnonymousUser
    void notAuthenticated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(403,response.getStatus());

    }

    @Test
    void findAll() throws Exception {
        var userList = List.of(responseDto);
        Page<UserResponseDto> page = new PageImpl<>(userList);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());

        when(userService.findAll(Optional.empty(),Optional.empty(),Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                get(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        PageResponse<UserResponseDto> pageRes = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                ()-> assertNotNull(pageRes),
                ()-> assertEquals(200,response.getStatus()),
                ()-> assertEquals(1,pageRes.content().size())
        );

        verify(userService,times(1)).findAll(Optional.empty(),Optional.empty(),Optional.empty(),pageable);
    }

    @Test
    void getById() throws Exception{
        var myLocalEndpoint = myEndPoint + "/1";

        when(userService.findById(anyLong())).thenReturn(infoResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        var res = mapper.readValue(response.getContentAsString(), UserInfoResponseDto.class);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(200,response.getStatus()),
                ()-> assertEquals(infoResponseDto,res)
        );

        verify(userService,times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFound() throws Exception {
        var myLocalEndpoint = myEndPoint + "/99";

        when(userService.findById(anyLong())).thenThrow(new UserNotFoundException("id " + 99));

        MockHttpServletResponse response = mockMvc.perform(
                get(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(404,response.getStatus());

        verify(userService,times(1)).findById(anyLong());
    }

    @Test
    void postUser() throws Exception {
        when(userService.save(any(UserRequestDto.class))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDto))
        ).andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponseDto.class);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(201,response.getStatus()),
                ()-> assertEquals(responseDto,res)
        );

        verify(userService,times(1)).save(any(UserRequestDto.class));
    }

    @Test
    void postUserBadRequestPassword() throws Exception {
        var userRequest = UserRequestDto.builder()
                .name("test")
                .username("test")
                .password("test")
                .email("test@example.com")
                .build();

        when(userService.save(userRequest)).thenReturn(responseDto);
        MockHttpServletResponse response = mockMvc.perform(
                post(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest))
        ).andReturn().getResponse();

        assertEquals(400,response.getStatus());
    }

    @Test
    void postUserBadRequestEmail() throws Exception {
        var userRequest = UserRequestDto.builder()
                .name("test")
                .username("test")
                .password("test1234")
                .email("test")
                .build();

        when(userService.save(userRequest)).thenReturn(responseDto);
        MockHttpServletResponse response = mockMvc.perform(
                post(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest))
        ).andReturn().getResponse();

        assertEquals(400,response.getStatus());
    }

    @Test
    void postUserBadRequestUsername() throws Exception {
        var userRequest = UserRequestDto.builder()
                .name("test")
                .password("test1234")
                .email("test@example.com")
                .build();

        when(userService.save(userRequest)).thenReturn(responseDto);
        MockHttpServletResponse response = mockMvc.perform(
                post(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest))
        ).andReturn().getResponse();

        assertEquals(400,response.getStatus());
    }

     @Test
    void postUserBadRequestName() throws Exception {
        var userRequest = UserRequestDto.builder()
                .username("test")
                .password("test1234")
                .email("test@example.com")
                .build();

        when(userService.save(userRequest)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(myEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest))
        ).andReturn().getResponse();

        assertEquals(400,response.getStatus());
    }

    @Test
    void putUser() throws Exception {
        var myLocalEndpoint = myEndPoint + "/1";

        when(userService.update(anyLong(),any(UserRequestDto.class))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDto))
        ).andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponseDto.class);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(200,response.getStatus()),
                ()-> assertEquals(responseDto,res)
        );

        verify(userService,times(1)).update(anyLong(),any(UserRequestDto.class));
    }

    @Test
    void updateUserNotFound() throws Exception{
        var myLocalEndpoint = myEndPoint + "/99";

        when(userService.update(anyLong(),any(UserRequestDto.class))).thenThrow(new UserNotFoundException("id " + 99));

        MockHttpServletResponse response = mockMvc.perform(
                put(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequestDto))
        ).andReturn().getResponse();

        assertEquals(404,response.getStatus());

        verify(userService,times(1)).update(anyLong(),any(UserRequestDto.class));
    }
    @Test
    void deleteUser() throws Exception {
        var myLocalEndpoint = myEndPoint + "/1";

        doNothing().when(userService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                delete(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(204,response.getStatus());

        verify(userService,times(1)).deleteById(anyLong());
    }

    @Test
    void deleteUserNotFound() throws Exception {
        var myLocalEndpoint = myEndPoint + "/99";

        doThrow(new UserNotFoundException("id " + 99)).when(userService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                delete(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(404,response.getStatus());
    }

    @Test
    @WithUserDetails("admin")
    void meProfile() throws Exception {
        var myLocalEndpoint = myEndPoint + "/me/profile";

        when(userService.findById(anyLong())).thenReturn(infoResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200,response.getStatus());
    }

    @Test
    @WithAnonymousUser
    void me_anonymousUser() throws Exception {
        var myLocalEndpoint = myEndPoint + "/me/profile";

        MockHttpServletResponse response = mockMvc.perform(
                get(myLocalEndpoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(403,response.getStatus());
    }
}