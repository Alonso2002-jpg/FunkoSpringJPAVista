package org.develop.FunkoSpringJpa.rest.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaExistException extends CategoriaException{
    public CategoriaExistException(String name) {
        super("Category already exists with name: " + name);
    }
}
