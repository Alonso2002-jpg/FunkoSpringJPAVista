package org.develop.FunkoSpringJpa.funko.services;

import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.services.CategoriaService;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketConfig;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketHandler;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoException;
import org.develop.FunkoSpringJpa.rest.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.rest.funko.repositories.FunkoRepository;
import org.develop.FunkoSpringJpa.rest.funko.services.FunkoServiceImpl;
import org.develop.FunkoSpringJpa.websockets.mapper.FunkoNotificacionMapper;
import org.develop.FunkoSpringJpa.websockets.model.Notificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {
    private Funko funko1, funko2;
    private Categoria categoria;
    private FunkoCreateDto funkoCreateDto;
    private FunkoUpdateDto funkoUpdateDto;
    @Mock
    FunkoRepository funkoRepository;
    @Mock
    CategoriaService categoriaService;
    @Mock
    FunkosMapper funkosMapper;
    @Mock
    FunkoNotificacionMapper funkoNotificacionMapper;
    WebSocketHandler webSocketHandler = mock(WebSocketHandler.class) ;
    @Mock
    WebSocketConfig webSocketConfig;
    @InjectMocks
    FunkoServiceImpl funkoService;

    @BeforeEach
    void setUp() {
    categoria = Categoria.builder()
            .nameCategory("MARVEL")
            .build();

    funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(10.0)
                .quantity(5)
                .category(Categoria.builder().build())
                .image("ola.jpg")
                .build();
    funko2 = Funko.builder()
                .id(2L)
                .name("Funko 2")
                .price(20.0)
                .quantity(10)
                .category(Categoria.builder().build())
                .image("ola2.jpg")
                .build();

    funkoCreateDto = FunkoCreateDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .category(1L)
            .build();

    funkoUpdateDto = FunkoUpdateDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .build();
    }

    @Test
    void getAllEmpty() {
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),pageable);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllName(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.of("funko1"),Optional.empty(),Optional.empty(),Optional.empty(),pageable);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllQuantity(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.of(10),Optional.empty(),Optional.empty(),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllPrice(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.empty(),Optional.of(100.0),Optional.empty(),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllCategory(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.empty(),Optional.empty(),Optional.of("series"),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllNameAndQuantity(){
         var listFunks = List.of(
                 funko1,
                 funko2
                 );
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);
        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        var res = funkoService.getAll(Optional.of("funko1"),Optional.of(10),Optional.empty(),Optional.empty(),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.getTotalElements()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllNameAndQuantityAndPrice(){
         var listFunks = List.of(
                 funko1,
                 funko2
                 );
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);
        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        var res = funkoService.getAll(Optional.of("funko1"),Optional.of(10),Optional.of(100.0),Optional.empty(),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.getTotalElements()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllNameAndQuantityAndPriceAndCategory(){
         var listFunks = List.of(
                 funko1,
                 funko2
                 );
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);
        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        var res = funkoService.getAll(Optional.of("funko1"),Optional.of(10),Optional.of(100.0),Optional.of("series"),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(2,res.getTotalElements()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllQuantityAndPrice(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.of(10),Optional.of(100.0),Optional.empty(),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );
        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllQuantityAndPriceAndCategory(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.of(10),Optional.of(100.0),Optional.of("series"),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );
        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void getAllPriceAndCategory(){
        var listFunks = List.of(funko1, funko2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> fknRes = new PageImpl<>(listFunks);

        when(funkoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fknRes);
        Page<Funko> res = funkoService.getAll(Optional.empty(),Optional.empty(),Optional.of(100.0),Optional.of("series"),pageable);

        assertAll(
                ()-> assertFalse(res.isEmpty()),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fknRes,res)
        );

        verify(funkoRepository,times(1)).findAll(any(Specification.class),any(Pageable.class));
    }
    @Test
    void findById() {
        when(funkoRepository.findById(1L)).thenReturn(Optional.of(funko1));

        var res =funkoService.findById(1L);

         assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(funko1,res)
        );

        verify(funkoRepository,times(1)).findById(1L);
    }

    @Test
    void findByIdError() {
        when(funkoRepository.findById(3L)).thenReturn(Optional.empty());

        var res = assertThrows(FunkoException.class,()-> funkoService.findById(3L));

        assertEquals("Funko not found with ID: " + 3, res.getMessage());

        verify(funkoRepository,times(1)).findById(3L);
    }

    @Test
    void save() throws IOException {
        when(funkoRepository.save(funko1)).thenReturn(funko1);
        when(categoriaService.getById(1L)).thenReturn(categoria);
        when(funkosMapper.toFunko(funkoCreateDto,categoria)).thenReturn(funko1);
        doNothing().when(webSocketHandler).sendMessage(any());
        var res = funkoService.save(funkoCreateDto);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(funko1,res)
        );

        verify(funkoRepository,times(1)).save(funko1);
        verify(funkosMapper,times(1)).toFunko(funkoCreateDto,categoria);
    }

    @Test
    void update() throws IOException {

        when(funkoRepository.findById(1L)).thenReturn(Optional.of(funko1));
        when(funkosMapper.toFunko(funkoUpdateDto,funko1,null)).thenReturn(funko1);
        when(funkoRepository.save(funko1)).thenReturn(funko1);
        doNothing().when(webSocketHandler).sendMessage(any());
        var res = funkoService.update(1L,funkoUpdateDto);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(funko1,res)
        );

        verify(funkoRepository,times(1)).save(funko1);
        verify(funkosMapper,times(1)).toFunko(funkoUpdateDto,funko1,null);
        verify(funkoRepository,times(1)).findById(1L);
    }

    @Test
    void deleteById() throws IOException {
        doNothing().when(funkoRepository).deleteById(1L);
        when(funkoRepository.findById(1L)).thenReturn(Optional.of(funko1));
        funkoService.deleteById(1L);
        doNothing().when(webSocketHandler).sendMessage(any());
        verify(funkoRepository,times(1)).deleteById(1L);
    }

    @Test
    void onChange() throws IOException {
        // Arrange
        doNothing().when(webSocketHandler).sendMessage("Hola");
        // Act
        funkoService.onChange(Notificacion.Tipo.CREATE, funko1);
    }
}