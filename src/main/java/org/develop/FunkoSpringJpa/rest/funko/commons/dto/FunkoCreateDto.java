package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FunkoCreateDto(
        @NotBlank(message = "Name must not be blank")
        String name,
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        @NotNull(message = "Price must not be null")
        Double price,
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        @NotNull(message = "Quantity must not be null")
        Integer quantity,
        String image,
        @NotNull(message = "Category must not be null")
        Long category
) {
}
