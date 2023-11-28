package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
@Schema(description = "Funko a Actualizar")
public record FunkoUpdateDto(
        @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        @Schema(description = "Nombre del Funko", example = "Funko de Amatista")
        String name,
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        @Schema(description = " Precio del Funko", example = "10.00")
        Double price,
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        @Schema(description = " Cantidad del Funko", example = "100")
        Integer quantity,
        @Pattern(regexp = ".*\\.(jpg|jpeg|png|gif|bmp)$", message = "IMG just can be a valid image")
        @Schema(description = "Imagen del Funko")
        String image,
        @Schema(description = "Categoria del Funko", example = "DISNEY")
        Long category) {
}
