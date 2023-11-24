package org.develop.FunkoSpringJpa.rest.users.commons.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.FunkoSpringJpa.rest.users.commons.models.Role;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Name cannot be empty")
    String name;
    @NotBlank(message = "Username cannot be empty")
    String username;
    @NotBlank(message = "Email cannot be empty")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    String email;
    @NotBlank(message = "Password cannot be empty")
    @Length(min = 8, message = "Password debe tener al menos 8 caracteres")
    String password;
    @NotNull(message = "Role cannot be empty")
    @Builder.Default
    Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    Boolean isActive = true;
}
