package org.develop.FunkoSpringJpa.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.FunkoSpringJpa.rest.auth.dto.JwtAuthResponseDto;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignInRequest;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignUpRequest;
import org.develop.FunkoSpringJpa.rest.auth.exceptions.UserInvalidPasswords;
import org.develop.FunkoSpringJpa.rest.auth.services.authentication.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthUserRestControllerTest {
    private final String myEndPoint = "/v1/auth";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    public AuthUserRestControllerTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void signUp() throws Exception {
        var userSignUpRequest = new UserSignUpRequest("Test","test","test@test.com","test12345","test12345");
        var jwtAutResponse = new JwtAuthResponseDto("token");

        var myLocalEndPoint = myEndPoint + "/signup";

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenReturn(jwtAutResponse);

        MockHttpServletResponse response = mockMvc.perform(
                post(myLocalEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userSignUpRequest))
        ).andReturn().getResponse();

        JwtAuthResponseDto res = mapper.readValue(response.getContentAsString(), JwtAuthResponseDto.class);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(200,response.getStatus()),
                ()-> assertEquals(jwtAutResponse,res)
        );

        verify(authenticationService,times(1)).signUp(any(UserSignUpRequest.class));
    }

    @Test
    void signUpPasswordDoNotMatch() throws Exception {
        var myLocalEndPoint = myEndPoint + "/signup";
        var userSignUpRequest = new UserSignUpRequest("Test","test","test@test.com","test12","test1");

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenThrow(new UserInvalidPasswords("Passwords don't match"));

        assertThrows(UserInvalidPasswords.class, () -> authenticationService.signUp(userSignUpRequest));

        verify(authenticationService,times(1)).signUp(any(UserSignUpRequest.class));
    }

    @Test
    void signUpUserOrEmailAlreadyExists() throws Exception {
        var myLocalEndPoint = myEndPoint + "/signup";
        var userSignUpRequest = new UserSignUpRequest("Test","test","test@test.com","test12345","test12345");

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenThrow(new UserInvalidPasswords("User or email already exists"));

        assertThrows(UserInvalidPasswords.class, () -> authenticationService.signUp(userSignUpRequest));

        verify(authenticationService,times(1)).signUp(any(UserSignUpRequest.class));
}

@Test
void signUpSomeAttributeEmpty() throws Exception {
    var myLocalEndPoint = myEndPoint + "/signup";
    var userSignUpRequest = new UserSignUpRequest("Test","test","test@test.com","test12345","");

    MockHttpServletResponse response = mockMvc.perform(
            post(myLocalEndPoint)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(userSignUpRequest))
    ).andReturn().getResponse();

    assertAll(
            ()-> assertNotNull(response),
            ()-> assertEquals(400,response.getStatus())
    );
}

    @Test
    void signIn() throws Exception {
        var userSignInRequest = new UserSignInRequest("test","test1234");
        var jwtAuthResponse = new JwtAuthResponseDto("token");

        var myLocalEndPoint = myEndPoint + "/signin";

        when(authenticationService.signIn(any(UserSignInRequest.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                post(myLocalEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userSignInRequest))
        ).andReturn().getResponse();

        JwtAuthResponseDto res = mapper.readValue(response.getContentAsString(), JwtAuthResponseDto.class);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(200,response.getStatus())
        );

        verify(authenticationService,times(1)).signIn(any(UserSignInRequest.class));
    }

    @Test
    void signInFailed() throws Exception {

        var userSignInRequest = new UserSignInRequest("test","<test1234>");

        when(authenticationService.signIn(any(UserSignInRequest.class))).thenThrow(new UserInvalidPasswords("Invalid credentials"));

        assertThrows(UserInvalidPasswords.class, () -> authenticationService.signIn(userSignInRequest));

        verify(authenticationService,times(1)).signIn(any(UserSignInRequest.class));
    }

    @Test
    void signInSomeAttributeEmpty() throws Exception {
        var myLocalEndPoint = myEndPoint + "/signin";
        var userSignInRequest = new UserSignInRequest("","");

        MockHttpServletResponse response = mockMvc.perform(
                post(myLocalEndPoint)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userSignInRequest))
        ).andReturn().getResponse();

        assertAll(
                ()-> assertNotNull(response),
                ()-> assertEquals(400,response.getStatus())
        );
    }
}