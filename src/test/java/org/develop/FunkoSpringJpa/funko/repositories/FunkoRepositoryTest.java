package org.develop.FunkoSpringJpa.funko.repositories;

import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.repositories.FunkoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = "spring.sql.init.mode = never")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class FunkoRepositoryTest {

    @Autowired
    private FunkoRepository funkoRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Funko funko1, funko2;;
    @BeforeEach
    public void setUp() throws Exception {
        Categoria categoria = Categoria.builder().nameCategory("OTROS").build();
        funkoRepository.deleteAll();
        funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(1.1)
                .quantity(5)
                .category(categoria)
                .image("ola.jpg")
                .build();
        funko2 = Funko.builder()
                .id(2L)
                .name("Funko 2")
                .price(2.2)
                .quantity(10)
                .category(categoria)
                .image("ola2.jpg")
                .build();

        entityManager.merge(categoria);
        entityManager.flush();
        entityManager.merge(funko1);
        entityManager.merge(funko2);
        entityManager.flush();

    }

    @AfterEach
    public void tearDown() throws Exception {
        funkoRepository.deleteAll();
    }

    @Test
    void getAll() {

        List<Funko> allfunks = funkoRepository.findAll();
        allfunks.forEach(System.out::println);
        assertAll(
                () -> assertTrue(allfunks.contains(funko1)),
                () -> assertTrue(allfunks.contains(funko2))
        );

    }

    @Test
    void findById() {
        Optional<Funko> funko = funkoRepository.findById(2L);

        assertAll(
                () -> assertTrue(funko.isPresent()),
                () -> assertEquals(funko2, funko.get())
        );
    }

    @Test
    void findByIdError(){
        Optional<Funko> funko = funkoRepository.findById(3L);
        assertTrue(funko.isEmpty());
    }
    @Test
    void save() {
        Funko funko = funkoRepository.save(Funko.builder().name("Funko Test").build());
        List<Funko> funkos = funkoRepository.findAll();

        assertAll(
                () -> assertNotNull(funko),
                () -> assertNotNull(funko.getId()),
                () -> assertEquals("Funko Test", funko.getName()),
                () -> assertTrue(funkos.contains(funko))
        );
    }

    @Test
    void deleteById() {
        funkoRepository.deleteById(funko1.getId());

        assertAll(
                () -> assertTrue(funkoRepository.findAll().contains(funko2))
        );
    }

    @Test
    void deleteAll(){
        funkoRepository.deleteAll();
        assertEquals(0, funkoRepository.findAll().size());
    }
}