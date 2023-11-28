package org.develop.FunkoSpringJpa.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserSignInRequest", description="Modelo para Logearse")
public class UserSignInRequest {
    @NotBlank(message = "username cannot be blank")
    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;
    @NotBlank(message = "password cannot be blank")
    @Schema(description = "Contraseña", example = "Admin1")
    private String password;
}
