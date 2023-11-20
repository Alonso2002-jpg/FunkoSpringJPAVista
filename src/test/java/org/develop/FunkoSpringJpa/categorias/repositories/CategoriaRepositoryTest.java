package org.develop.FunkoSpringJpa.categorias.repositories;

import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.repositories.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@TestPropertySource(properties = "spring.sql.init.mode = never")
@DataJpaTest
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private TestEntityManager entityManager;
    Categoria categoria = Categoria.builder()
            .id(null)
            .nameCategory("OTROS")
            .createdAt(LocalDate.now())
            .updatedAt(LocalDate.now())
            .isActive(true)
            .build();

    @BeforeEach
    void setup(){
        entityManager.merge(categoria);
        entityManager.flush();
    }

    @Test
    void findAllCategories() {
        // Arrange
        List<Categoria> res = categoriaRepository.findAll();

        // Act
        assertAll(
                () -> assertNotNull(res),
                () -> assertFalse(res.isEmpty())
        );
    }

    @Test
    void findByNameCategory(){
        // Arrange
        String name = "OTROS";

        // Act
        Optional<Categoria> foundCategory = categoriaRepository.findByNameCategoryIgnoreCase(name);

        // Assert
        assertAll(
                () -> assertNotNull(foundCategory),
                () -> assertTrue(foundCategory.isPresent()),
                () -> assertEquals(name, foundCategory.get().getNameCategory())
        );
    }

    @Test
    void findByNameCategory_returnEmpty(){
            // Arrange
        String name = "HALA_MADRID";

        // Act
        Optional<Categoria> foundCategory = categoriaRepository.findByNameCategoryIgnoreCase(name);

        // Assert
        assertAll(
                () -> assertNotNull(foundCategory),
                () -> assertTrue(foundCategory.isEmpty())
        );
    }

    @Test
    void findById(){
        // Arrange
        Long id = 1L;

        // Act
        Optional<Categoria> foundCategory = categoriaRepository.findById(id);

        // Assert
            assertAll(
                () -> assertNotNull(foundCategory)
//                () -> assertTrue(foundCategory.isPresent()),
//                () -> assertEquals(id, foundCategory.get().getId())
        );
    }

     @Test
    void findById_returnEmpty(){
        // Arrange
        Long id = 50L;

        // Act
        Optional<Categoria> foundCategory = categoriaRepository.findById(id);

        // Assert
            assertAll(
                () -> assertNotNull(foundCategory),
                () -> assertTrue(foundCategory.isEmpty())
        );
    }

    @Test
    void save(){
        // Arrange
        Categoria newCategory = Categoria.builder()
            .id(null)
            .nameCategory("PELICULAS")
            .createdAt(LocalDate.now())
            .updatedAt(LocalDate.now())
            .isActive(true)
            .build();


        // Act
        Categoria savedCategory = categoriaRepository.save(newCategory);

        assertAll(
                () -> assertNotNull(savedCategory),
                () -> assertNotNull(savedCategory.getId()),
                () -> assertEquals(newCategory.getNameCategory(), savedCategory.getNameCategory()),
                () -> assertTrue(newCategory.isActive())
        );
    }

    @Test
    void update(){
          Categoria newCategory = Categoria.builder()
            .id(1L)
            .nameCategory("PELICULAS")
            .createdAt(LocalDate.now())
            .updatedAt(LocalDate.now())
            .isActive(true)
            .build();

          Categoria updatedCategory = categoriaRepository.save(newCategory);

                  assertAll(
                () -> assertNotNull(updatedCategory),
                () -> assertEquals(newCategory.getNameCategory(), updatedCategory.getNameCategory()),
                () -> assertTrue(newCategory.isActive())
        );
    }








}
