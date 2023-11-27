package org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PedidoNotFoundException extends PedidoException{
    public PedidoNotFoundException(String message) {
        super("Pedido not found with : " + message);
    }
}
