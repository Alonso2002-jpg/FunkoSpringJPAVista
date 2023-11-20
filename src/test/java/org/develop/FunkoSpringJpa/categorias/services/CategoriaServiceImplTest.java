package org.develop.FunkoSpringJpa.categorias.services;

import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.exceptions.CategoriaException;
import org.develop.FunkoSpringJpa.rest.categorias.mappers.CategoriaMapper;
import org.develop.FunkoSpringJpa.rest.categorias.repositories.CategoriaRepository;
import org.develop.FunkoSpringJpa.rest.categorias.services.CategoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    private Categoria categoria;
    private CategoriaCreateDto categoriaCreateDto;
    private CategoriaUpdateDto categoriaUpdateDto;

    @Mock
    CategoriaRepository categoriaRepository;
    @Mock
    CategoriaMapper categoriaMapper;

    @InjectMocks
    CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp(){
        categoria = Categoria.builder()
                .nameCategory("OTROS")
                .isActive(true)
                .build();

        categoriaCreateDto = CategoriaCreateDto.builder()
                .nameCategory(categoria.getNameCategory())
                .isActive(categoria.isActive())
                .build();
        categoriaUpdateDto = CategoriaUpdateDto.builder()
                .nameCategory(categoria.getNameCategory())
                .isActive(categoria.isActive())
                .build();
    }

    @Test
    void getAllEmpty() {
        var listCategorias = List.of(categoria);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> fkCategoria = new PageImpl<>(listCategorias);
        when(categoriaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(fkCategoria);

        Page<Categoria> res = categoriaService.getAll(Optional.empty(),Optional.empty(), pageable);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fkCategoria,res)
        );

        verify(categoriaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getAllName(){
        var listCategorias = List.of(categoria);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> fkCategoria = new PageImpl<>(listCategorias);
        when(categoriaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(fkCategoria);

        Page<Categoria> res = categoriaService.getAll(Optional.of("Otros"),Optional.empty(), pageable);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fkCategoria,res)
        );

        verify(categoriaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

        @Test
    void getAllIsActive(){
        var listCategorias = List.of(categoria);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> fkCategoria = new PageImpl<>(listCategorias);
        when(categoriaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(fkCategoria);

        Page<Categoria> res = categoriaService.getAll(Optional.empty(),Optional.of(true), pageable);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fkCategoria,res)
        );

        verify(categoriaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getAllNameAndIsActive(){
        var listCategorias = List.of(categoria);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> fkCategoria = new PageImpl<>(listCategorias);
        when(categoriaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(fkCategoria);

        Page<Categoria> res = categoriaService.getAll(Optional.of("Otros"),Optional.of(true), pageable);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertFalse(res.isEmpty()),
                ()-> assertEquals(fkCategoria,res)
        );

        verify(categoriaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void getById() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        var res = categoriaService.getById(1L);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(categoria,res)
        );

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void getByIdError(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaException.class, ()-> categoriaService.getById(1L));

        assertEquals("Category not found with ID: " + 1, res.getMessage());

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void save() {
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaRepository.findByNameCategoryIgnoreCase(categoria.getNameCategory())).thenReturn(Optional.empty());
        when(categoriaMapper.toCategoria(categoriaCreateDto)).thenReturn(categoria);

        var res = categoriaService.save(categoriaCreateDto);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(categoria,res)
        );

        verify(categoriaRepository, times(1)).save(categoria);
        verify(categoriaRepository, times(1)).findByNameCategoryIgnoreCase(categoria.getNameCategory());
        verify(categoriaMapper, times(1)).toCategoria(categoriaCreateDto);
    }

    @Test
    void saveError(){
        when(categoriaRepository.findByNameCategoryIgnoreCase(categoria.getNameCategory())).thenReturn(Optional.of(categoria));

        var res = assertThrows(CategoriaException.class, ()-> categoriaService.save(categoriaCreateDto));

        assertEquals("Category already exists with name: " + categoria.getNameCategory(), res.getMessage());

        verify(categoriaRepository, times(1)).findByNameCategoryIgnoreCase(categoria.getNameCategory());
    }

    @Test
    void deleteById() {
        doNothing().when(categoriaRepository).deleteById(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.deleteById(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void deleteByIdError(){
        when(categoriaRepository.findById(100L)).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaException.class, ()-> categoriaService.deleteById(100L));

        assertEquals("Category not found with ID: " + 100, res.getMessage());

        verify(categoriaRepository, times(1)).findById(100L);
    }

    @Test
    void update() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toCategoria(categoriaUpdateDto,categoria)).thenReturn(categoria);

        var res = categoriaService.update(1L,categoriaUpdateDto);

        assertAll(
                ()-> assertNotNull(res),
                ()-> assertEquals(categoria,res),
                ()-> assertEquals(categoria.getNameCategory(),res.getNameCategory())
        );

        verify(categoriaRepository, times(1)).save(categoria);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaMapper, times(1)).toCategoria(categoriaUpdateDto,categoria);
    }

    @Test
    void updateError(){
        when(categoriaRepository.findById(100L)).thenReturn(Optional.empty());

        var res = assertThrows(CategoriaException.class, ()-> categoriaService.update(100L,categoriaUpdateDto));

        assertEquals("Category not found with ID: " + 100, res.getMessage());

        verify(categoriaRepository, times(1)).findById(100L);
    }
    @Test
    void deleteAll() {
        doNothing().when(categoriaRepository).deleteAll();

        categoriaService.deleteAll();

        verify(categoriaRepository, times(1)).deleteAll();
    }
}