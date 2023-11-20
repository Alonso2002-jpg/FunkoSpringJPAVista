package org.develop.FunkoSpringJpa.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternal extends StorageException{
    public StorageInternal(String message) {
        super(message);
    }
}
