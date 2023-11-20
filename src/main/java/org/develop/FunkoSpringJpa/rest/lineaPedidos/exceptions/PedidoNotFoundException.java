package org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions;

public class PedidoNotFoundException extends PedidoException{
    public PedidoNotFoundException(String message) {
        super("Pedido not found with : " + message);
    }
}
