package org.develop.FunkoSpringJpa.rest.users.exceptions;

public class UserNotFoundException extends UserException{
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
