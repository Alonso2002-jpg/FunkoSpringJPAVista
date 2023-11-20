package org.develop.FunkoSpringJpa.rest.funko.mappers;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FunkosMapper {

    public Funko toFunko(FunkoCreateDto funkoCreateDto, Categoria categoria){
        return Funko.builder()
                .name(funkoCreateDto.name())
                .price(funkoCreateDto.price())
                .quantity(funkoCreateDto.quantity())
                .category(categoria)
                .image(funkoCreateDto.image() == null ? Funko.IMG_DEFAULT : funkoCreateDto.image())
                .build();
    }

    public Funko toFunko(FunkoUpdateDto funkoUpdateDto, Funko funko, Categoria categoria){
        return Funko.builder()
                .id(funko.getId())
                .name(funkoUpdateDto.name() == null ? funko.getName() : funkoUpdateDto.name())
                .price(funkoUpdateDto.price() == null ? funko.getPrice() : funkoUpdateDto.price())
                .quantity(funkoUpdateDto.quantity() == null ? funko.getQuantity() : funkoUpdateDto.quantity())
                .image(funkoUpdateDto.image() == null ? funko.getImage() : funkoUpdateDto.image())
                .category(categoria == null ? funko.getCategory() : categoria)
                .createdAt(funko.getCreatedAt())
                .build();
    }

    public FunkoUpdateDto toUpdateDto(Funko funko){
        return FunkoUpdateDto.builder()
                .image(funko.getImage())
                .build();

    }
    public Funko toFunko(FunkoResponseDto funkoResponseDto, Long id){
        return Funko.builder()
                .id(id)
                .name(funkoResponseDto.name())
                .price(funkoResponseDto.price())
                .quantity(funkoResponseDto.quantity())
                .image(funkoResponseDto.image())
                .category(funkoResponseDto.category())
                .build();
    }
    public FunkoResponseDto toResponseDto(Funko funko){
        return FunkoResponseDto.builder()
                .name(funko.getName())
                .price(funko.getPrice())
                .quantity(funko.getQuantity())
                .image(funko.getImage())
                .category(funko.getCategory())
                .build();
    }

    public List<FunkoResponseDto> toResponseDtoList(List<Funko> funkos){
        return funkos.stream().map(this::toResponseDto).toList();
    }

    public Page<FunkoResponseDto> toPageResponse(Page<Funko> funkosPages){
        return funkosPages.map(this::toResponseDto);
    }

}
