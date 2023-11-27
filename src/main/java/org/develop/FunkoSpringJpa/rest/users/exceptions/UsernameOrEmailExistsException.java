package org.develop.FunkoSpringJpa.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameOrEmailExistsException extends UserException{
    public UsernameOrEmailExistsException(String message) {
        super("Username or email " + message + " already exists");
    }
}
