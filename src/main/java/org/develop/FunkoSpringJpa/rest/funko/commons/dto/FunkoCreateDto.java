package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Funko a Crear")
public record FunkoCreateDto(
        @NotBlank(message = "Name must not be blank")
        @Schema(description = "Nombre del Funko", example = "Funko de Amatista")
        String name,
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        @NotNull(message = "Price must not be null")
        @Schema(description = "Precio del Funko", example = "10.00")
        Double price,
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        @NotNull(message = "Quantity must not be null")
        @Schema(description = " Cantidad del Funko", example = "100")
        Integer quantity,
        String image,
        @NotNull(message = "Category must not be null")
        @Schema(description = "Categoria del Funko", example = "1")
        Long category
) {
}
