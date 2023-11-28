package org.develop.FunkoSpringJpa.users.services;

import org.develop.FunkoSpringJpa.rest.lineaPedidos.repositories.PedidoRepository;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserInfoResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserRequestDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.develop.FunkoSpringJpa.rest.users.exceptions.UserNotFoundException;
import org.develop.FunkoSpringJpa.rest.users.exceptions.UsernameOrEmailExistsException;
import org.develop.FunkoSpringJpa.rest.users.mappers.UserMapper;
import org.develop.FunkoSpringJpa.rest.users.repositories.UserRepository;
import org.develop.FunkoSpringJpa.rest.users.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final UserRequestDto userRequestDto = UserRequestDto.builder()
            .username("test")
            .email("test@example.com")
            .build();
    private final User user = User.builder()
            .id(1L)
            .username("test")
            .email("test@example.com")
            .build();
    private final UserResponseDto userResponseDto = UserResponseDto.builder()
            .username("test")
            .email("test@example.com")
            .build();
    private final UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
            .username("test")
            .email("test@example.com")
            .build();

    @Mock
    private UserRepository userRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void findAllWithouFilters() {
        List<User> users = List.of(new User(), new User());
        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.findAll(Optional.empty(),Optional.empty(),Optional.empty(),Pageable.unpaged());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );

        verify(userRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void findById() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidoRepository.findPedidosByIdUsuario(userId)).thenReturn(List.of());
        when(userMapper.toUserInfoResponse(any(User.class),anyList())).thenReturn(userInfoResponseDto);

        UserInfoResponseDto result = userService.findById(userId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponseDto.getUsername(), result.getUsername()),
                () -> assertEquals(userResponseDto.getEmail(), result.getEmail())
        );

        verify(userRepository,times(1)).findById(userId);
        verify(pedidoRepository,times(1)).findPedidosByIdUsuario(userId);
        verify(userMapper,times(1)).toUserInfoResponse(any(User.class),anyList());
    }

    @Test
    void findByIdNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> userService.findById(userId));

        verify(userRepository,times(1)).findById(userId);
    }

    @Test
    void save() {
        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(),anyString())).thenReturn(Optional.empty());
        when(userMapper.toUser(userRequestDto)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDto);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto result = userService.save(userRequestDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponseDto.getUsername(), result.getUsername()),
                () -> assertEquals(userResponseDto.getEmail(), result.getEmail())
        );

        verify(userRepository,times(1)).findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(),anyString());
        verify(userMapper,times(1)).toUser(userRequestDto);
        verify(userMapper,times(1)).toUserResponse(user);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    void saveUsernameOrEmailExists() {
        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(),anyString())).thenReturn(Optional.of(user));

        assertThrows(UsernameOrEmailExistsException.class,() -> userService.save(userRequestDto));
    }
    @Test
    void update() {

        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(),anyString())).thenReturn(Optional.empty());
        when(userMapper.toUser(user,userRequestDto,userId)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponseDto);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto result = userService.update(userId,userRequestDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponseDto.getUsername(), result.getUsername()),
                () -> assertEquals(userResponseDto.getEmail(), result.getEmail())
        );

        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,times(1)).findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(),anyString());
        verify(userMapper,times(1)).toUser(user,userRequestDto,userId);
        verify(userMapper,times(1)).toUserResponse(user);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    void updateUsernameOrEmailExists() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString(),anyString())).thenReturn(Optional.of(user));

        assertThrows(UsernameOrEmailExistsException.class,() -> userService.update(userId,userRequestDto));
    }

    @Test
    void updateNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> userService.update(userId,userRequestDto));
    }
    @Test
    void deleteById() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteById(userId);

        verify(userRepository,times(1)).findById(userId);
    }

    @Test
    void deleteByIdNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> userService.deleteById(userId));
    }
}