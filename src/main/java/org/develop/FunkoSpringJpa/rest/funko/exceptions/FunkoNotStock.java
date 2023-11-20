package org.develop.FunkoSpringJpa.rest.funko.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FunkoNotStock extends FunkoException {
    public FunkoNotStock(Long id) {
        super("Funko with id " + id + " is not in stock");
    }
}
