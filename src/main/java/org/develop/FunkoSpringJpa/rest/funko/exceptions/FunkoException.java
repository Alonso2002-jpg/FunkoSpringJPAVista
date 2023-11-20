package org.develop.FunkoSpringJpa.rest.funko.exceptions;


public abstract class FunkoException extends RuntimeException{
    FunkoException(String message){
        super(message);
    }
}
