package org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions;

public class PedidoEmptyException extends PedidoException{
    public PedidoEmptyException(String id) {
        super("Pedido with id " + id + " is empty");
    }
}
