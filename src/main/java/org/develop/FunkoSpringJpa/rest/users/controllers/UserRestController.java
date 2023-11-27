package org.develop.FunkoSpringJpa.rest.users.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.develop.FunkoSpringJpa.pages.models.PageResponse;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.commons.models.Pedido;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.services.PedidoService;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserInfoResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserRequestDto;
import org.develop.FunkoSpringJpa.rest.users.commons.dto.UserResponseDto;
import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.develop.FunkoSpringJpa.rest.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("${api.version}/users")
@PreAuthorize("hasRole('USER')")
public class UserRestController {
    private final UserService userService;
    private final PedidoService pedidoService;

    @Autowired
    public UserRestController(UserService userService, PedidoService pedidoService) {
        this.userService = userService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponseDto>> findAll(@RequestParam(required = false)Optional<String> username,
                                                                @RequestParam(required = false)Optional<String> email,
                                                                @RequestParam(required = false)Optional<Boolean> isActive,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                 @RequestParam(defaultValue = "ASC") String direction) {
        log.info("Obteniendo todos los usuarios");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<UserResponseDto> pageResult = userService.findAll(username, email, isActive, PageRequest.of(page, size, sort));

        return ResponseEntity.ok(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponseDto> getById(@PathVariable("id") Long id) {
        log.info("Obteniendo usuario con id: " + id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> postUser(@RequestBody UserRequestDto user) {
        log.info("Creando usuario: " + user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> putUser(@PathVariable("id") Long id, @RequestBody UserRequestDto user) {
        log.info("Actualizando usuario con id: " + id);
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        log.info("Eliminando usuario con id: " + id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfoResponseDto> meProfile(@AuthenticationPrincipal User user) {
        log.info("Obteniendo perfil de usuario");
        return ResponseEntity.ok(userService.findById(user.getId()));
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> meProfileUpdate(@AuthenticationPrincipal User user, @RequestBody UserRequestDto userRequestDto) {
        log.info("Actualizando perfil de usuario");
        return ResponseEntity.ok(userService.update(user.getId(), userRequestDto));
    }

    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> meProfileDelete(@AuthenticationPrincipal User user) {
        log.info("Eliminando perfil de usuario");
        userService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<Pedido>> mePedidos(@AuthenticationPrincipal User user,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String direction) {
        log.info("Obteniendo pedidos de usuario con Id: " + user.getId());
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(pedidoService.findByIdUsuario(user.getId(), pageable), sortBy, direction));
    }

    @GetMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pedido> mePedido(@AuthenticationPrincipal User user, @PathVariable("id") ObjectId id) {
        log.info("Obteniendo pedido con id: " + id);
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @PostMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pedido> postPedido(@AuthenticationPrincipal User user, @Valid @RequestBody Pedido pedido) {
        log.info("Creando pedido: " + pedido);
        pedido.setIdUsuario(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));
    }

    @PutMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pedido> putPedido(@AuthenticationPrincipal User user, @PathVariable("id") ObjectId id, @Valid @RequestBody Pedido pedido) {
        log.info("Actualizando pedido con id: " + id);
        pedido.setIdUsuario(user.getId());
        return ResponseEntity.ok(pedidoService.update(id, pedido));
    }

    @DeleteMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deletePedido(@AuthenticationPrincipal User user, @PathVariable("id") ObjectId id) {
        log.info("Eliminando pedido con id: " + id);
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
