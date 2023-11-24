package org.develop.FunkoSpringJpa.rest.users.commons.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.FunkoSpringJpa.rest.users.commons.models.Role;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    Long id;
    String name;
    String username;
    String email;
    String password;
    @Builder.Default
    Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    Boolean isActive = true;
}
