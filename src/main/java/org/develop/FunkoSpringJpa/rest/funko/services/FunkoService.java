package org.develop.FunkoSpringJpa.rest.funko.services;

import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FunkoService {
    Page<Funko> getAll(Optional<String> name, Optional<Integer> quantity, Optional<Double> price, Optional<String> category, Pageable pageable);
    Funko findById(Long id);
    Funko save(FunkoCreateDto t);

    void deleteById(Long id);
    Funko update(Long id, FunkoUpdateDto funko);
    Funko updateImage(Long id, MultipartFile file);

    void deleteAll();

}
