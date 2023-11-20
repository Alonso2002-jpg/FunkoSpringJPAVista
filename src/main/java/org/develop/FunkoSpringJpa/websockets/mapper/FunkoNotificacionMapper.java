package org.develop.FunkoSpringJpa.websockets.mapper;

import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.websockets.dto.FunkoNotificacionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class FunkoNotificacionMapper {
    public FunkoNotificacionResponseDto toFunkoNotificacionMapper(Funko funko) {
        return new FunkoNotificacionResponseDto(
                funko.getName(),
                funko.getPrice(),
                funko.getQuantity(),
                funko.getImage(),
                funko.getCategory().toString(),
                funko.getCreatedAt().toString(),
                funko.getUpdatedAt().toString()
        );
    }
}
