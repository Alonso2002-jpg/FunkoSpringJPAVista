package org.develop.FunkoSpringJpa.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends UserException{
    public UserNotFoundException(String message) {
        super("User with" + message + " not found");
    }
}
