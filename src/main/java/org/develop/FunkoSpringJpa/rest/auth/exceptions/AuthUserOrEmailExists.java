package org.develop.FunkoSpringJpa.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthUserOrEmailExists extends AuthException{
    public AuthUserOrEmailExists(String message) {
        super(message);
    }
}
