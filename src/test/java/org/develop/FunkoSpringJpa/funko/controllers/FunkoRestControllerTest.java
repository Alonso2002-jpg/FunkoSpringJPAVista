package org.develop.FunkoSpringJpa.funko.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoResponseDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoNotFound;
import org.develop.FunkoSpringJpa.rest.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.rest.funko.services.FunkoService;
import org.develop.FunkoSpringJpa.pages.models.PageResponse;
import org.develop.FunkoSpringJpa.storage.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", roles = {"ADMIN","USER"})
class FunkoRestControllerTest {

    private final String initEndPoint = "/funkos";
    private Funko funko1, funko2;
    private FunkoResponseDto funkoResponseDto,funkoResponseDto2;
    private StorageService storageService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private FunkoService funkosService;
    @MockBean
    private FunkosMapper funkosMapper;
    @Autowired
    private JacksonTester<FunkoCreateDto> jsonFunkoCreateDto;
    @Autowired
    private JacksonTester<FunkoUpdateDto> jsonFunkoUpdateDto;

    @Autowired
    public FunkoRestControllerTest(FunkoService funkoService,FunkosMapper funkosMapper){
        this.funkosService = funkoService;
        this.funkosMapper = funkosMapper;
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
    Categoria categoria1 = Categoria.builder().nameCategory("MARVEL").build();
    Categoria categoria2 = Categoria.builder().nameCategory("DISNEY").build();

    funko1 = Funko.builder()
                .id(1L)
                .name("Funko 1")
                .price(10.0)
                .quantity(5)
                .category(categoria1)
                .image("ola.jpg")
                .build();
    funko2 = Funko.builder()
                .id(2L)
                .name("Funko 2")
                .price(20.0)
                .quantity(10)
                .category(categoria2)
                .image("ola2.jpg")
                .build();
    funkoResponseDto = FunkoResponseDto.builder()
            .name(funko1.getName())
            .price(funko1.getPrice())
            .quantity(funko1.getQuantity())
            .category(funko1.getCategory())
            .image(funko1.getImage())
            .build();
    funkoResponseDto2 = FunkoResponseDto.builder()
            .name(funko2.getName())
            .price(funko2.getPrice())
            .quantity(funko2.getQuantity())
            .category(funko2.getCategory())
            .image(funko2.getImage())
            .build();
    }

    @Test
    void getFunkos() throws Exception {
        List<Funko> funkos = List.of(funko1,funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto, funkoResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(initEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(2,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name()),
                () -> assertEquals(funkoResponseDto2.name(),funkosResList.content().get(1).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);

    }
        @Test
    void getFunkosName() throws Exception {
        var localEndPoint = initEndPoint + "?name=funko1";

        List<Funko> funkos = List.of(funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.of("funko1"), Optional.empty(),Optional.empty(), Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.of("funko1"), Optional.empty(),Optional.empty(), Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void getFunkosQuantity() throws Exception {
        var localEndPoint = initEndPoint + "?quantity=10";

        List<Funko> funkos = List.of(funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto,funkoResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.empty(), Optional.of(10),Optional.empty(), Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(2,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name()),
                () -> assertEquals(funkoResponseDto2.name(),funkosResList.content().get(1).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(), Optional.of(10),Optional.empty(), Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void getFunkosPrice() throws Exception {
        var localEndPoint = initEndPoint + "?price=10";

        List<Funko> funkos = List.of(funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.empty(), Optional.empty(),Optional.of(10.0), Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto2.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(), Optional.empty(),Optional.of(10.0), Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void getFunkosCategory() throws Exception {
        var localEndPoint = initEndPoint + "?category=series";

        List<Funko> funkos = List.of(funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.empty(), Optional.empty(),Optional.empty(), Optional.of("series"),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto2.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(), Optional.empty(),Optional.empty(), Optional.of("series"),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void getFunkosNameAndQuantity() throws Exception {
        var localEndPoint = initEndPoint + "?name=funko1&quantity=10";

        List<Funko> funkos = List.of(funko1);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.of("funko1"), Optional.of(10),Optional.empty(), Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.of("funko1"), Optional.of(10),Optional.empty(), Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }

    @Test
    void getFunkosNameAndQuantityAndPrice() throws Exception {
        var localEndPoint = initEndPoint + "?name=funko1&quantity=10&price=100.0";

        List<Funko> funkos = List.of(funko1);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.of("funko1"), Optional.of(10),Optional.of(100.0), Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.of("funko1"), Optional.of(10),Optional.of(100.0), Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
        @Test
    void getFunkosNameAndQuantityAndPriceAndCategory() throws Exception {
        var localEndPoint = initEndPoint + "?name=funko1&quantity=10&price=100.0&category=series";

        List<Funko> funkos = List.of(funko1);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.of("funko1"), Optional.of(10),Optional.of(100.0), Optional.of("series"),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.of("funko1"), Optional.of(10),Optional.of(100.0), Optional.of("series"),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
        @Test
    void getFunkosQuantityAndPrice() throws Exception {
        var localEndPoint = initEndPoint + "?quantity=10&price=100.0";

        List<Funko> funkos = List.of(funko1,funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto,funkoResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.empty(), Optional.of(10),Optional.of(100.0), Optional.empty(),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(2,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name()),
                () -> assertEquals(funkoResponseDto2.name(),funkosResList.content().get(1).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(), Optional.of(10),Optional.of(100.0), Optional.empty(),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
            @Test
    void getFunkosQuantityAndCategory() throws Exception {
        var localEndPoint = initEndPoint + "?quantity=10&category=series";

        List<Funko> funkos = List.of(funko1,funko2);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto,funkoResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll(Optional.empty(), Optional.of(10),Optional.empty(), Optional.of("series"),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(2,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name()),
                () -> assertEquals(funkoResponseDto2.name(),funkosResList.content().get(1).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(), Optional.of(10),Optional.empty(), Optional.of("series"),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void getFunkosPriceAndCategory() throws Exception {
        var localEndPoint = initEndPoint + "?price=100.0&category=series";

        List<Funko> funkos = List.of(funko1);
        List<FunkoResponseDto> funkosRes = List.of(funkoResponseDto);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Funko> responsePage = new PageImpl<>(funkos);
        Page<FunkoResponseDto> responseDtosPage = new PageImpl<>(funkosRes);

        when(funkosService.getAll( Optional.empty(),Optional.empty(),Optional.of(100.0),Optional.of("series"),pageable)).thenReturn(responsePage);
        when(funkosMapper.toPageResponse(responsePage)).thenReturn(responseDtosPage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<FunkoResponseDto> funkosResList = mapper.readValue(response.getContentAsString(),new TypeReference<>(){});

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(),response.getStatus()),
                () -> assertEquals(1,funkosResList.content().size()),
                () -> assertEquals(funkoResponseDto.name(),funkosResList.content().get(0).name())
        );

        verify(funkosService,times(1)).getAll(Optional.empty(), Optional.empty(),Optional.of(100.0),Optional.of("series"),pageable);
        verify(funkosMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void getFunko() throws Exception {
        var localEndPoint = initEndPoint + "/1";

        when(funkosService.findById(anyLong())).thenReturn(funko1);
        when(funkosMapper.toResponseDto(funko1)).thenReturn(funkoResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponseDto funkoResponseDto = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructType(FunkoResponseDto.class));

        assertAll(
                () -> assertEquals(200,response.getStatus()),
                () -> assertEquals(funko1.getName(),funkoResponseDto.name())
        );

        verify(funkosService,times(1)).findById(anyLong());
    }

    @Test
    void getFunkoError() throws Exception {
        var localEndPoint = initEndPoint + "/1";

        when(funkosService.findById(anyLong())).thenThrow(new FunkoNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
            get(localEndPoint)
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404,response.getStatus());
    }

    @Test
    void postFunkos() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(10.0)
                .quantity(5)
                .category(1L)
                .image("ola.jpg")
                .build();

        when(funkosService.save(any(FunkoCreateDto.class))).thenReturn(funko1);
        when(funkosMapper.toResponseDto(funko1)).thenReturn(funkoResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
            post(initEndPoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(funkoCreDto))
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponseDto funkoRes = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructType(FunkoResponseDto.class));

        assertAll(
                () -> assertEquals(201,response.getStatus()),
                () -> assertEquals(funko1.getName(),funkoRes.name())
        );
        verify(funkosService,times(1)).save(any(FunkoCreateDto.class));
    }

    @Test
    void postFunkosErrorName() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .price(10.0)
                .quantity(5)
                .category(1L)
                .image("ola.jpg")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
            post(initEndPoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400,response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Name must not be blank"))
        );
    }

    @Test
    void postFunkosErrorPrice() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(-10.0)
                .quantity(5)
                .category(1L)
                .image("ola.jpg")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
            post(initEndPoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400,response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Price must be greater than or equal to 0"))
        );
    }

    @Test
    void postFunkosErrorQuantityNull() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(10.0)
                .category(1L)
                .image("ola.jpg")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
            post(initEndPoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400,response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Quantity must not be null"))
        );
    }

    @Test
    void postFunkosErrorQuantity() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(10.0)
                .quantity(-1)
                .category(1L)
                .image("ola.jpg")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(initEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Quantity must be greater than or equal to 0"))
        );
    }

    @Test
    void postFunkosErrorImg() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(10.0)
                .quantity(-1)
                .category(1L)
                .image("ola.pdf")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(initEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
    }

    @Test
    void postFunkosErrorCategoryNull() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(10.0)
                .quantity(-1)
                .image("ola.pdf")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(initEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Category must not be null"))
        );
    }

    @Test
    void postFunkosErrorCategory() throws Exception {
        FunkoCreateDto funkoCreDto = FunkoCreateDto.builder()
                .name("Funko")
                .price(10.0)
                .quantity(-1)
                .image("ola.pdf")
                .category(99L)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(initEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFunkoCreateDto.write(funkoCreDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
    }
    @Test
    void putFunko() throws Exception {
                var localEndPoint = initEndPoint + "/1";

        FunkoUpdateDto funkoDto = FunkoUpdateDto.builder()
                                    .name("Funko")
                                    .price(10.0)
                                    .quantity(1)
                                    .image("ola.jpg")
                                    .category(1L)
                                    .build();
        when(funkosService.update(anyLong(),any(FunkoUpdateDto.class))).thenReturn(funko1);
        when(funkosMapper.toResponseDto(funko1)).thenReturn(funkoResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFunkoUpdateDto.write(funkoDto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponseDto funkoResponseDto = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructType(FunkoResponseDto.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(funko1.getName(),funkoResponseDto.name())
        );

        verify(funkosService, times(1)).update(anyLong(),any(FunkoUpdateDto.class));
    }

    @Test
    void putFunkoNotFound() throws Exception {
        var localEndPoint = initEndPoint + "/1";

        FunkoUpdateDto funkoDto = FunkoUpdateDto.builder()
                                    .name("Funko")
                                    .price(10.0)
                                    .quantity(1)
                                    .image("ola.jpg")
                                    .category(1L)
                                    .build();
        when(funkosService.update(anyLong(),any(FunkoUpdateDto.class))).thenThrow(new FunkoNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFunkoUpdateDto.write(funkoDto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void putFunkoErrorPrice() throws Exception {
        var localEndPoint = initEndPoint + "/1";

                FunkoUpdateDto funkoDto = FunkoUpdateDto.builder()
                                    .name("Funko")
                                    .price(-10.0)
                                    .quantity(1)
                                    .image("ola.jpg")
                                    .category(1L)
                                    .build();
        when(funkosService.update(anyLong(),any(FunkoUpdateDto.class))).thenThrow(new FunkoNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFunkoUpdateDto.write(funkoDto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Price must be greater than or equal to 0"))
        );
    }

    @Test
    void putFunkoErrorQuantity() throws Exception {
        var localEndPoint = initEndPoint + "/1";

                FunkoUpdateDto funkoDto = FunkoUpdateDto.builder()
                                    .name("Funko")
                                    .price(10.0)
                                    .quantity(-1)
                                    .image("ola.jpg")
                                    .category(1L)
                                    .build();
        when(funkosService.update(anyLong(),any(FunkoUpdateDto.class))).thenThrow(new FunkoNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFunkoUpdateDto.write(funkoDto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Quantity must be greater than or equal to 0"))
        );
    }

    @Test
    void putFunkoErrorImg() throws Exception {
        var localEndPoint = initEndPoint + "/1";

                FunkoUpdateDto funkoDto = FunkoUpdateDto.builder()
                                    .name("Funko")
                                    .price(10.0)
                                    .quantity(1)
                                    .image("ola.pdf")
                                    .category(1L)
                                    .build();
        when(funkosService.update(anyLong(),any(FunkoUpdateDto.class))).thenThrow(new FunkoNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFunkoUpdateDto.write(funkoDto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("IMG just can be a valid image"))
        );
    }

    @Test
    void putFunkoErrorCategory() throws Exception {
        var localEndPoint = initEndPoint + "/1";

        FunkoUpdateDto funkoDto = FunkoUpdateDto.builder()
                                    .name("Funko")
                                    .price(10.0)
                                    .quantity(1)
                                    .image("ola.jpg")
                                    .category(1L)
                                    .build();
        when(funkosService.update(anyLong(),any(FunkoUpdateDto.class))).thenThrow(new FunkoNotFound(1L));

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFunkoUpdateDto.write(funkoDto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(404, response.getStatus())
         );
    }
    @Test
    void deleteFunko() throws Exception {
        var localEndPoint = initEndPoint + "/1";

        doNothing().when(funkosService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                delete(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(funkosService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteFunkoNotFound() throws Exception {
        var localEndPoint = initEndPoint + "/100";

        doThrow(new FunkoNotFound(100L)).when(funkosService).deleteById(100L);

        MockHttpServletResponse response = mockMvc.perform(
                delete(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(funkosService, times(1)).deleteById(100L);
    }

    @Test
    void putImage() throws Exception {
        var localEndPoint = initEndPoint + "/imagen/1";

        FunkoUpdateDto fkUpd = new FunkoUpdateDto(
                funko1.getName(),
                funko1.getPrice(),
                funko1.getQuantity(),
                funko1.getImage(),
                1L
        );
        when(funkosService.findById(1L)).thenReturn(funko1);
        when(funkosService.update(1L,fkUpd)).thenReturn(funko1);
        when(funkosMapper.toResponseDto(funko1)).thenReturn(funkoResponseDto);
        when(funkosMapper.toUpdateDto(funko1)).thenReturn(fkUpd);

        // Crear un archivo simulado
        MockMultipartFile file = new MockMultipartFile(
                "file", // Nombre del parámetro del archivo en el controlador
                "filename.jpg", // Nombre del archivo
                MediaType.IMAGE_JPEG_VALUE, // Tipo de contenido del archivo
                "contenido del archivo".getBytes() // Contenido del archivo
        );

        // Crear una solicitud PATCH multipart con el fichero simulado
        MockHttpServletResponse response = mockMvc.perform(
                multipart(localEndPoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


//        FunkoResponseDto res = mapper.readValue(response.getContentAsString(), FunkoResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus())
        );
//        // Verify
//        verify(funkosService,times(1)).findById(1L);
//        verify(funkosMapper, times(1)).toResponseDto(funko1);
//        verify(funkosMapper, times(1)).toUpdateDto(funko1);
    }
}