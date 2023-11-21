package org.develop.FunkoSpringJpa.rest.funko.commons.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record FunkoUpdateDto(
        @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        Double price,
        @Min(value = 0, message = "Quantity must be greater than or equal to 0")
        Integer quantity,
        @Pattern(regexp = ".*\\.(jpg|jpeg|png|gif|bmp)$", message = "IMG just can be a valid image")
        String image,
        Long category) {
}
