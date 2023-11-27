package org.develop.FunkoSpringJpa.rest.auth.services.authentication;

import org.develop.FunkoSpringJpa.rest.auth.dto.JwtAuthResponseDto;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignInRequest;
import org.develop.FunkoSpringJpa.rest.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponseDto signUp(UserSignUpRequest signUpRequestRequest);
    JwtAuthResponseDto signIn(UserSignInRequest signInRequest);
}
