package org.develop.FunkoSpringJpa.rest.funko.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.rest.funko.services.FunkoService;
import org.develop.FunkoSpringJpa.pages.models.PageResponse;
import org.develop.FunkoSpringJpa.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("${api.version}/funkos")
@Tag(name = "Funkos", description = "EndPoint de Funkos de la aplicacion")
public class FunkoRestController {
        private final FunkoService funkoService;
        private final FunkosMapper funkosMapper;
        private final StorageService storageService;

    @Autowired
    public FunkoRestController(FunkoService funkoService, FunkosMapper funkosMapper,StorageService storageService) {
        this.funkoService =funkoService;
        this.funkosMapper = funkosMapper;
        this.storageService = storageService;
    }

    @Operation(summary = "Obtiene todos los Funkos", description = "Obtiene una lista de Funkos")
    @Parameters({
            @Parameter(name = "name", description = "Filtro por nombre"),
            @Parameter(name = "quantity", description = "Filtro por cantidad"),
            @Parameter(name = "price", description = "Filtro por precio"),
            @Parameter(name = "category", description = "Filtro por categoria"),
            @Parameter(name = "page", description = "Numero de Pagina"),
            @Parameter(name = "size", description = "Tamaño de Pagina"),
            @Parameter(name = "sortBy", description = "Ordenamiento"),
            @Parameter(name = "direction", description = "Direccion de Ordenacion")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de Funkos"),
    })
    @GetMapping
    public ResponseEntity<PageResponse<FunkoResponseDto>> getFunkos(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Integer> quantity,
            @RequestParam(required = false) Optional<Double> price,
            @RequestParam(required = false) Optional<String> category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Obteniendo los Funkos por : " + name + ", " + quantity + ", " + price + ", " + category);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(funkosMapper.toPageResponse(funkoService.getAll(name,quantity,price, category,pageable)),sortBy,direction));
    }

    @Operation(summary = "Obtiene un Funko por su ID", description = "Obtiene un Funko basado en su ID")
    @Parameters({
            @Parameter(name = "id", description = "ID del Funko", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funko encontrado"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FunkoResponseDto> getFunko(@PathVariable Long id) {
        return ResponseEntity.ok(funkosMapper.toResponseDto(funkoService.findById(id)));
    }

    @Operation(summary = "Crea un nuevo Funko", description = "Crea un nuevo Funko en el sistema")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Funko a agregar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funko creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "403", description = "Acceso no permitido")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FunkoResponseDto> postFunkos(@Valid @RequestBody FunkoCreateDto funko) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funkosMapper.toResponseDto(funkoService.save(funko)));
    }

    @Operation(summary = "Actualiza un Funko existente", description = "Actualiza un Funko existente en el sistema")
    @Parameters({
            @Parameter(name = "id", description = "ID del Funko", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Funko a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funko actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "403", description = "Acceso no permitido"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FunkoResponseDto> putFunko(@PathVariable long id, @Valid @RequestBody FunkoUpdateDto funko) {
        return ResponseEntity.ok(funkosMapper.toResponseDto(funkoService.update(id,funko)));
    }


    @Operation(summary = "Elimina un Funko por ID", description = "Elimina un Funko del sistema según su ID")
    @Parameters({
        @Parameter(name = "id", description = "ID del Funko a eliminar", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funko eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso no permitido"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteFunko(
            @PathVariable long id) {
        funkoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualiza la imagen de un Funko por ID", description = "Actualiza la imagen de un Funko en el sistema según su ID")
    @Parameters({
        @Parameter(name = "id", description = "ID del Funko cuya imagen se actualizará",example = "1", required = true),
        @Parameter(name = "file", description = "Imagen de Funko", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen de Funko actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "403", description = "Acceso no permitido"),
            @ApiResponse(responseCode = "404", description = "Funko no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "imagen/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FunkoResponseDto> putImagen(@PathVariable long id, @RequestPart("file") MultipartFile file){
        if (!file.isEmpty()){
             return ResponseEntity.ok(funkosMapper.toResponseDto(funkoService.updateImage(id,file)));
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagen no enviada");
        }
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
