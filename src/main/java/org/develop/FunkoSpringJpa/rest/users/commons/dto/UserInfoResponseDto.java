package org.develop.FunkoSpringJpa.rest.users.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.FunkoSpringJpa.rest.users.commons.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private Boolean isActive = true;
    @Builder.Default
    private List<String> pedidos = new ArrayList<>();
}
