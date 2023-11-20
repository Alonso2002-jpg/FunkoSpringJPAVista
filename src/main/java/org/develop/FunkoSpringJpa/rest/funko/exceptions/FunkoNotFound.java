package org.develop.FunkoSpringJpa.rest.funko.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FunkoNotFound extends FunkoException{
    public FunkoNotFound(Long id) {
        super("Funko not found with ID: " + id);
    }
}
