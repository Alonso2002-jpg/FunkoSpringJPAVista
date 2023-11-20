package org.develop.FunkoSpringJpa.rest.categorias.commons.dto;

import lombok.Builder;

@Builder
public record CategoriaResponseDto(
        String nameCategory,
        Boolean isActive
) {
}
