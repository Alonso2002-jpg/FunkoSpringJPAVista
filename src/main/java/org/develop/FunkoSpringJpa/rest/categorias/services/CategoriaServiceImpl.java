package org.develop.FunkoSpringJpa.rest.categorias.services;

import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaCreateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.dto.CategoriaUpdateDto;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.exceptions.CategoriaExistException;
import org.develop.FunkoSpringJpa.rest.categorias.exceptions.CategoriaNotFoundException;
import org.develop.FunkoSpringJpa.rest.categorias.mappers.CategoriaMapper;
import org.develop.FunkoSpringJpa.rest.categorias.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = "categorias")
public class CategoriaServiceImpl implements CategoriaService {
    private CategoriaRepository categoriaRepository;
    private CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }


    @Override
    public Page<Categoria> getAll(Optional<String> name, Optional<Boolean> isActive, Pageable pageable) {
        Specification<Categoria> specByName = ((root, query, criteriaBuilder) ->
                name.map(n -> criteriaBuilder.like(root.get("nameCategory"), "%" + n + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<Categoria> spectByIsActive = (root, query, criteriaBuilder) ->
                isActive.map(isAc -> criteriaBuilder.equal(root.get("isActive"), isAc))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Categoria> criteria = Specification.where(specByName)
                .and(spectByIsActive);

        return categoriaRepository.findAll(criteria, pageable);
    }

    @Override
    public Categoria getById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    @Override
    public Categoria save(CategoriaCreateDto t) {
        if (categoriaRepository.findByNameCategoryIgnoreCase(t.nameCategory()).isPresent()){
            throw new CategoriaExistException(t.nameCategory());
        }
        return categoriaRepository.save(categoriaMapper.toCategoria(t));
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria update(Long id, CategoriaUpdateDto funko) {
        return categoriaRepository.save(categoriaMapper.toCategoria(funko, getById(id)));
    }

    @Override
    public void deleteAll() {
        categoriaRepository.deleteAll();
    }
}
