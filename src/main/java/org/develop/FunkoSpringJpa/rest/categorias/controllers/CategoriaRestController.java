package org.develop.FunkoSpringJpa.rest.categorias.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaResponseDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.rest.categorias.mappers.CategoriaMapper;
import org.develop.FunkoSpringJpa.rest.categorias.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("v1/funkos/categorias")
public class CategoriaRestController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;
    @Autowired
    public CategoriaRestController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDto>> getCategorias(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Boolean> isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo los Funkos por : " + name + ", " + isActive);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(categoriaMapper.toPageResponse(categoriaService.getAll(name,isActive,pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> getCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaMapper.toResponseDto(categoriaService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> postFunko(@Valid @RequestBody CategoriaCreateDto categoriaCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toResponseDto(categoriaService.save(categoriaCreateDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> putFunko(@PathVariable long id, @Valid @RequestBody CategoriaUpdateDto categoriaUpdateDto) {
        return ResponseEntity.ok(categoriaMapper.toResponseDto(categoriaService.update(id,categoriaUpdateDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFunko(@PathVariable long id) {
        categoriaService.deleteById(id);
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
