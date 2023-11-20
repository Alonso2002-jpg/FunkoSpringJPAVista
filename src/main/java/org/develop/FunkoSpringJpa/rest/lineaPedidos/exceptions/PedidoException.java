package org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions;

public abstract class PedidoException extends RuntimeException{
    public PedidoException(String message){
        super(message);
    }
}
