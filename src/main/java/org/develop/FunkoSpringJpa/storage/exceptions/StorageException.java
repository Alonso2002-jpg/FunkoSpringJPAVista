package org.develop.FunkoSpringJpa.storage.exceptions;

import org.develop.FunkoSpringJpa.storage.service.StorageService;

public abstract class StorageException extends RuntimeException{
    public StorageException(String message){
        super(message);
    }
}
