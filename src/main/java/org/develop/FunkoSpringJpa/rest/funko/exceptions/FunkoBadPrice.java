package org.develop.FunkoSpringJpa.rest.funko.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FunkoBadPrice extends FunkoException{
    public FunkoBadPrice(Long id) {
        super("Funko with id " + id + " has a bad price or his price doesnt match with his real price");
    }
}
