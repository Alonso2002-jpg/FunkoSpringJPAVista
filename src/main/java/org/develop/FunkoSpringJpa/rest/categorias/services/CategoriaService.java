package org.develop.FunkoSpringJpa.rest.categorias.services;

import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoriaService {

    Page<Categoria> getAll(Optional<String> name, Optional<Boolean> isActive, Pageable pageable);
    Categoria getById(Long id);
    Categoria save(CategoriaCreateDto t);
    void deleteById(Long id);
    Categoria update(Long id, CategoriaUpdateDto funko);

    void deleteAll();
}
