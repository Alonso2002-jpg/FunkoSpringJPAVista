package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;

@Builder
@Schema(description = "Funko de Respuesta")
public record FunkoResponseDto(
        @Schema(description = "Nombre del Funko", example = "Funko de Amatista")
        String name,
        @Schema(description = " Precio del Funko", example = "10.00")
        Double price,
        @Schema(description = " Cantidad del Funko", example = "100")
        Integer quantity,
        @Schema(description = "Imagen del Funko")
        String image,
        @Schema(description = "Categoria del Funko", example = "DISNEY")
        Categoria category
) {
}
