package org.develop.FunkoSpringJpa.rest.auth.services.authentication;

import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.auth.dto.JwtAuthResponseDto;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignInRequest;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignUpRequest;
import org.develop.FunkoSpringJpa.rest.auth.exceptions.AuthSignInInvalid;
import org.develop.FunkoSpringJpa.rest.auth.exceptions.UserInvalidPasswords;
import org.develop.FunkoSpringJpa.rest.auth.repositories.AuthRepository;
import org.develop.FunkoSpringJpa.rest.auth.services.jwt.JwtService;
import org.develop.FunkoSpringJpa.rest.users.commons.models.Role;
import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.develop.FunkoSpringJpa.rest.users.exceptions.UsernameOrEmailExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public JwtAuthResponseDto signUp(UserSignUpRequest signUpRequestRequest) {
        log.info("signUp: {}", signUpRequestRequest);
        if (signUpRequestRequest.getPassword().contentEquals(signUpRequestRequest.getPasswordComprobacion())){
            User user = User.builder()
                    .name(signUpRequestRequest.getName())
                    .username(signUpRequestRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequestRequest.getPassword()))
                    .email(signUpRequestRequest.getEmail())
                    .roles(Stream.of(Role.USER).collect(Collectors.toSet()))
                    .build();
            try {
                var usersaved = authRepository.save(user);
                return JwtAuthResponseDto.builder()
                        .token(jwtService.generateToken(usersaved))
                        .build();
            } catch (DataIntegrityViolationException e){
                throw new UsernameOrEmailExistsException("Username or email already exists");
            }
        }else {
            throw new UserInvalidPasswords("Passwords don't match");
        }
    }

    @Override
    public JwtAuthResponseDto signIn(UserSignInRequest signInRequest) {
        log.info("signIn: {}", signInRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        var user = authRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new AuthSignInInvalid("User or Password incorrect"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponseDto.builder().token(jwt).build();
    }
}
