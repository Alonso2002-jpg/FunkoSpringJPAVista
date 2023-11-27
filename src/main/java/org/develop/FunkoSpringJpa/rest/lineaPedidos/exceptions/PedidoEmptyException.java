package org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoEmptyException extends PedidoException{
    public PedidoEmptyException(String id) {
        super("Pedido with id " + id + " is empty");
    }
}
