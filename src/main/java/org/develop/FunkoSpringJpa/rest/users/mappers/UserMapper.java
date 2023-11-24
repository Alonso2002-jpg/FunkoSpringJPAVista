package org.develop.FunkoSpringJpa.rest.users.mappers;

import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserInfoResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserRequestDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public User toUser(UserRequestDto request) {
        return User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isActive(request.getIsActive())
                .build();
    }

    public User toUser(UserRequestDto request, Long id) {
        return User.builder()
                .id(id)
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isActive(request.getIsActive())
                .build();
    }

    public UserResponseDto toUserResponse(User user){
        return UserResponseDto.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();
    }

    public UserInfoResponseDto toUserInfoResponse(User user, List<String> pedidos){
        return UserInfoResponseDto.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .pedidos(pedidos)
                .build();

    }
}
