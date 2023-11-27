package org.develop.FunkoSpringJpa.rest.lineaPedidos.commons.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record Cliente(
    @Length(min = 3, message = "El nombre debe tener mas de 3 caracteres")
    String nombreCompleto,
    @Email(message = "El email no es valido")
    String email,
    @NotBlank(message = "El telefono no puede estar vacio")
    String telefono,
    @NotNull(message = "La direccion no puede ser nula")
    Direccion direccion
) {
}
