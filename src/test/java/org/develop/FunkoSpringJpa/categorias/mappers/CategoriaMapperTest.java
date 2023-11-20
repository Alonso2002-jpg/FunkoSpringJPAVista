package org.develop.FunkoSpringJpa.categorias.mappers;

import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaResponseDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.mappers.CategoriaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaMapperTest {
    private CategoriaMapper categoriaMapper;
    private Categoria categoria;
    private CategoriaCreateDto categoriaCreateDto;
    private CategoriaUpdateDto categoriaUpdateDto;
    private CategoriaResponseDto categoriaResponseDto;

    @BeforeEach
    void setUp(){
        categoriaMapper = new CategoriaMapper();

        categoria = Categoria.builder()
                .nameCategory("MARVEL")
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

        categoriaResponseDto = CategoriaResponseDto.builder()
                .nameCategory(categoria.getNameCategory())
                .isActive(categoria.isActive())
                .build();
    }

    @Test
    void toCategoriaCreate() {
        Categoria catTest = categoriaMapper.toCategoria(categoriaCreateDto);

        assertAll(
                () -> assertEquals(categoria.getNameCategory(), catTest.getNameCategory()),
                () -> assertEquals(categoria.isActive(), catTest.isActive())
        );
    }

    @Test
    void toCreate() {
        CategoriaCreateDto catCreateTest = categoriaMapper.toCreate(categoria);

        assertAll(
                () -> assertEquals(categoria.getNameCategory(), catCreateTest.nameCategory())
        );
    }

    @Test
    void ToCategoriaUpdate() {
        Categoria catTest = categoriaMapper.toCategoria(categoriaUpdateDto,categoria);

        assertAll(
                () -> assertEquals(categoria.getNameCategory(), catTest.getNameCategory()),
                () -> assertEquals(categoria.isActive(), catTest.isActive())
        );
    }

    @Test
    void toUpdateDto() {
        CategoriaUpdateDto catUpdateTest = categoriaMapper.toUpdateDto(categoria);

        assertAll(
                () -> assertEquals(categoria.getNameCategory(), catUpdateTest.nameCategory()),
                () -> assertEquals(categoria.isActive(), catUpdateTest.isActive())
        );
    }

    @Test
    void ToCategoriaResponse() {
        Categoria catTest = categoriaMapper.toCategoria(categoriaResponseDto,1L);

        assertAll(
                () -> assertEquals(1L, catTest.getId()),
                () -> assertEquals(categoria.getNameCategory(), catTest.getNameCategory()),
                () -> assertEquals(categoria.isActive(), catTest.isActive())
        );

    }

    @Test
    void toResponseDto() {
        CategoriaResponseDto catResTest = categoriaMapper.toResponseDto(categoria);

        assertAll(
                () -> assertEquals(categoria.getNameCategory(), catResTest.nameCategory()),
                () -> assertEquals(categoria.isActive(), catResTest.isActive())
        );
    }

    @Test
    void toResponseDtoList() {
        List<CategoriaResponseDto> catResTest = categoriaMapper.toResponseDtoList(List.of(categoria));

        assertAll(
                () -> assertFalse(catResTest.isEmpty()),
                () -> assertEquals(categoria.getNameCategory(), catResTest.get(0).nameCategory()),
                () -> assertEquals(categoria.isActive(), catResTest.get(0).isActive())
        );
    }

    @Test
    void toPageResponse() {
        Page<CategoriaResponseDto> pageCatTest = categoriaMapper.toPageResponse(new PageImpl<>(List.of(categoria)));

        assertAll(
                () -> assertFalse(pageCatTest.isEmpty()),
                () -> assertEquals(categoria.getNameCategory(), pageCatTest.getContent().get(0).nameCategory()),
                () -> assertEquals(categoria.isActive(), pageCatTest.getContent().get(0).isActive())
        );
    }
}