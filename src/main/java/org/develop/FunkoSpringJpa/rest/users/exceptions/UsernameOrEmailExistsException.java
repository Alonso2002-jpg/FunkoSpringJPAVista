package org.develop.FunkoSpringJpa.rest.users.exceptions;

public class UsernameOrEmailExistsException extends UserException{
    public UsernameOrEmailExistsException(String message) {
        super("Username or email " + message + " already exists");
    }
}
