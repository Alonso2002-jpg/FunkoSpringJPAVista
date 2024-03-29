package org.develop.FunkoSpringJpa.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    @NotBlank(message = "Nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 8, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 8, message = "Password de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;
}
