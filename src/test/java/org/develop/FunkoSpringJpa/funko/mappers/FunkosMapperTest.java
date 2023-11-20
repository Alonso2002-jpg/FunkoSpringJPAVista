package org.develop.FunkoSpringJpa.funko.mappers;

import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.mappers.FunkosMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunkosMapperTest {
    private FunkosMapper funkosMapper;
    private Funko funko1;
    private Categoria categoria;
    private FunkoCreateDto funkoCreateDto;
    private FunkoUpdateDto funkoUpdateDto;
    private FunkoResponseDto funkoResponseDto;

    @BeforeEach
    void setUp() {
    funkosMapper = new FunkosMapper();
    categoria = Categoria.builder()
            .nameCategory("MARVEL")
            .isActive(true)
            .build();

    funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(1.1)
                .quantity(5)
                .category(categoria)
                .image("ola.jpg")
                .build();

    funkoCreateDto = FunkoCreateDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .image(funko1.getImage())
            .category(1L)
            .build();

    funkoUpdateDto = FunkoUpdateDto.builder()
            .name("Funko UPD")
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .category(2L)
            .build();

    funkoResponseDto = FunkoResponseDto.builder()
            .name("Funko Create")
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .image(funko1.getImage())
            .category(funko1.getCategory())
            .build();
    }

    @Test
    void toFunkoCreate() {
    Funko funko = funkosMapper.toFunko(funkoCreateDto,categoria);

    assertAll(
            () -> assertNotNull(funko),
            () -> assertNull(funko.getId()),
            () -> assertEquals(funkoCreateDto.name(), funko.getName())

    );
    }


    @Test
    void ToFunkoUpdate() {
    Funko funko = funkosMapper.toFunko(funkoUpdateDto, funko1,categoria);

    assertAll(
            () -> assertNotNull(funko),
            () -> assertNotNull(funko.getQuantity()),
            () -> assertEquals(funkoUpdateDto.name(), funko.getName()),
            () -> assertEquals(funko.getQuantity(), funko1.getQuantity())
    );
    }


    @Test
    void ToFunkoResponse() {
        Funko funko = funkosMapper.toFunko(funkoResponseDto, 1L);

        assertAll(
            () -> assertNotNull(funko),
            () -> assertNotNull(funko.getId()),
            () -> assertEquals(funkoResponseDto.name(), funko.getName()),
            () -> assertEquals(1L, funko.getId())
        );
    }

    @Test
    void toResponseDto() {
        FunkoResponseDto fkDto = funkosMapper.toResponseDto(funko1);
         assertAll(
            () -> assertNotNull(fkDto),
            () -> assertEquals(funko1.getName(), fkDto.name())
        );
    }

    @Test
    void toPageResponse(){
        Page<Funko> fkPageFun = new PageImpl<>(List.of(funko1));
        Page<FunkoResponseDto> fkResDto = funkosMapper.toPageResponse(fkPageFun);

        assertAll(
                () -> assertNotNull(fkResDto),
                () -> assertEquals(1,fkResDto.getTotalElements()),
                () -> assertEquals(funko1.getName(),fkResDto.getContent().get(0).name())
        );
    }
}