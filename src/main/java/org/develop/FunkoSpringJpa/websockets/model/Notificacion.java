package org.develop.FunkoSpringJpa.websockets.model;

public record Notificacion<T>(
        String entity,
        Tipo tipo,
        T data,
        String createdAt
) {

    public enum Tipo{CREATE, UPDATE, DELETE}
}
