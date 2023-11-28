package org.develop.FunkoSpringJpa.auth.services;

import org.develop.FunkoSpringJpa.rest.auth.dto.JwtAuthResponseDto;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignInRequest;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignUpRequest;
import org.develop.FunkoSpringJpa.rest.auth.exceptions.AuthSignInInvalid;
import org.develop.FunkoSpringJpa.rest.auth.exceptions.UserInvalidPasswords;
import org.develop.FunkoSpringJpa.rest.auth.repositories.AuthRepository;
import org.develop.FunkoSpringJpa.rest.auth.services.authentication.AuthenticationService;
import org.develop.FunkoSpringJpa.rest.auth.services.authentication.AuthenticationServiceImpl;
import org.develop.FunkoSpringJpa.rest.auth.services.jwt.JwtService;
import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.develop.FunkoSpringJpa.rest.users.exceptions.UsernameOrEmailExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthRepository  authRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void signUp() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setName("Test");
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordComprobacion("password");
        request.setEmail("test@example.com");

        User userSaved = new User();
        when(authRepository.save(any(User.class))).thenReturn(userSaved);

        String token = "token";
        when(jwtService.generateToken(userSaved)).thenReturn(token);

        JwtAuthResponseDto response = authenticationService.signUp(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken())
        );

        verify(authRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(userSaved);
    }

    @Test
    public void signUpPasswordDoNotMatch() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setName("Test");
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordComprobacion("password2");
        request.setEmail("test@example.com");

        assertThrows(UserInvalidPasswords.class, () -> authenticationService.signUp(request));
    }

    @Test
    void signUpUsernameOrEmailExists() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setName("Test");
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordComprobacion("password");
        request.setEmail("test@example.com");

        when(authRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UsernameOrEmailExistsException.class, () -> authenticationService.signUp(request));
    }
    @Test
    void signIn() {
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        String token = "token";
        when(jwtService.generateToken(user)).thenReturn(token);

        JwtAuthResponseDto response = authenticationService.signIn(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken())
        );

        verify(authRepository, times(1)).findByUsername(request.getUsername());
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void signInUserNotFound() {
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(AuthSignInInvalid.class, () -> authenticationService.signIn(request));
    }
}