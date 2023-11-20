package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import lombok.Builder;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;

@Builder
public record FunkoResponseDto(
        String name,
        Double price,
        Integer quantity,
        String image,
        Categoria category
) {
}
