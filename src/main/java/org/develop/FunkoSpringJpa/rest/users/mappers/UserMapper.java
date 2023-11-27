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

    public User toUser(User user,UserRequestDto request, Long id) {
        return User.builder()
                .id(id)
                .name(request.getName() == null ? user.getName() : request.getName())
                .username(request.getUsername() == null ? user.getUsername() : request.getUsername())
                .email(request.getEmail() == null ? user.getEmail() : request.getEmail())
                .password(request.getPassword() == null ? user.getPassword() : request.getPassword())
                .roles(request.getRoles() == null ? user.getRoles() : request.getRoles())
                .isActive(request.getIsActive() == null ? user.getIsActive() : request.getIsActive())
                .build();
    }

    public UserResponseDto toUserResponse(User user){
        return UserResponseDto.builder()
                .id(user.getId())
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
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .pedidos(pedidos)
                .build();

    }
}
