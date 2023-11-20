package org.develop.FunkoSpringJpa.rest.categorias.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CategoriaUpdateDto(
        @Pattern(regexp = "^(SERIE|DISNEY|SUPERHEROES|PELICULAS|OTROS)$", message = "Category must be SERIE, DISNEY, SUPERHEROES, PELICULAS or OTROS")
        String nameCategory,
        Boolean isActive
) {
}
