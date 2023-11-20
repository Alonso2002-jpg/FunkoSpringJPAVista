package org.develop.FunkoSpringJpa.rest.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends CategoriaException{
    public CategoriaNotFoundException(Long id) {
        super("Category not found with ID: " + id);
    }
}
