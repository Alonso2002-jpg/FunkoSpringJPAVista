package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record FunkoUpdateDto(
        String name,
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        Double price,
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        Integer quantity,
        @Pattern(regexp = ".*\\.(jpg|jpeg|png|gif|bmp)$", message = "IMG just can be a valid image")
        String image,
        Long category) {
}
