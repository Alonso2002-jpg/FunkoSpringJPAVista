package org.develop.FunkoSpringJpa.websockets.dto;

public record FunkoNotificacionResponseDto(
        String name,
        Double price,
        Integer quantity,
        String image,
        String categoria,
        String createdAt,
        String updatedAt
) {
}
