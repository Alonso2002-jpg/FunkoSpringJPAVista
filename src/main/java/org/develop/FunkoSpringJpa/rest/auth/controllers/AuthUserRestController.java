package org.develop.FunkoSpringJpa.rest.auth.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.auth.dto.JwtAuthResponseDto;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignInRequest;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignUpRequest;
import org.develop.FunkoSpringJpa.rest.auth.services.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("${api.version}/auth")
public class AuthUserRestController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthUserRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponseDto> signup(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        log.info("Signup request: {}", userSignUpRequest);
        return ResponseEntity.ok(authenticationService.signUp(userSignUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponseDto> signin(@RequestBody UserSignInRequest userSignInRequest) {
        log.info("Signin request: {}", userSignInRequest);
        return ResponseEntity.ok(authenticationService.signIn(userSignInRequest));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
