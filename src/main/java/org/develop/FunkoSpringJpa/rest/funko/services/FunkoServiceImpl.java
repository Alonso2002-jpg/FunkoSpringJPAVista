package org.develop.FunkoSpringJpa.rest.funko.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.services.CategoriaService;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketConfig;
import org.develop.FunkoSpringJpa.config.websockets.WebSocketHandler;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoNotFound;
import org.develop.FunkoSpringJpa.rest.funko.mappers.FunkosMapper;
import org.develop.FunkoSpringJpa.rest.funko.repositories.FunkoRepository;
import org.develop.FunkoSpringJpa.storage.service.StorageService;
import org.develop.FunkoSpringJpa.websockets.dto.FunkoNotificacionResponseDto;
import org.develop.FunkoSpringJpa.websockets.mapper.FunkoNotificacionMapper;
import org.develop.FunkoSpringJpa.websockets.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = "funkos")
public class FunkoServiceImpl implements FunkoService{
    private final FunkoRepository funkoRepository;
    private final CategoriaService categoriaService;
    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private final FunkoNotificacionMapper funkoNotificacionMapper;
    private final FunkosMapper funkosMapper;
    private final ObjectMapper mapper;
    private WebSocketHandler webSocketService;

    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository,
                            FunkosMapper funkosMapper,
                            CategoriaService categoriaService,
                            StorageService storageService,
                            WebSocketConfig webSocketConfig,
                            FunkoNotificacionMapper funkoNotificacionMapper
    ) {
    this.funkoRepository = funkoRepository;
    this.funkosMapper = funkosMapper;
    this.categoriaService = categoriaService;
    this.storageService = storageService;
    this.webSocketConfig = webSocketConfig;
    this.funkoNotificacionMapper = funkoNotificacionMapper;

    webSocketService = webSocketConfig.webSocketFunkosHandler();
    mapper = new ObjectMapper();
    }
    @Override
    public Page<Funko> getAll(Optional<String> name, Optional<Integer> quantity, Optional<Double> price, Optional<String> category, Pageable pageable) {
        Specification<Funko> specByName = (root, query, criteriaBuilder) ->
            name.map( na -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + na.toLowerCase() + "%"))
                    .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Funko> specByQuantity = (root, query, criteriaBuilder) ->
            quantity.map( q -> criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), q))
                    .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Funko> specByPrice = (root, query, criteriaBuilder) ->
            price.map( p -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), p))
                    .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Funko> specByCategory = (root, query, criteriaBuilder) ->
                category.map(c ->{
                Join<Funko, Categoria> categoriaJoin = root.join("category");
                return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategory")), "%" + c.toLowerCase() + "%");
            }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Funko> criterio = Specification.where(specByName)
                .and(specByQuantity)
                .and(specByCategory)
                .and(specByPrice);

        return funkoRepository.findAll(criterio, pageable);
    }


    @Override
    @Cacheable
    public Funko findById(Long id) {
        return funkoRepository.findById(id).orElseThrow(() -> new FunkoNotFound(id));
    }

    @Override
    @CachePut
    public Funko save(FunkoCreateDto t) {
        var saved = funkoRepository.save(funkosMapper.toFunko(t, categoriaService.getById(t.category())));
        onChange(Notificacion.Tipo.CREATE,saved);
        return saved;
    }


    @Override
    public void deleteAll() {
        funkoRepository.deleteAll();
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        var funkoID = findById(id);
        funkoRepository.deleteById(id);
        onChange(Notificacion.Tipo.DELETE, funkoID);
    }

    @Override
    @CachePut
    public Funko update(Long id, FunkoUpdateDto funko) {
        var funkoUpd = funkoRepository.save(
                funkosMapper.toFunko(
        funko,
        findById(id),
        funko.category() == null ? null : categoriaService.getById(funko.category())));
        onChange(Notificacion.Tipo.UPDATE, funkoUpd);
        return funkoUpd;
    }

    @Override
    @CachePut
    public Funko updateImage(Long id, MultipartFile file) {
        Funko actualFunko = findById(id);
        String img =storageService.store(file);
        String urlName = storageService.getUrl(img).replace(" ","");
        if (!actualFunko.getImage().equals(Funko.IMG_DEFAULT)) {
            storageService.delete(actualFunko.getImage());
        }
        actualFunko.setImage(urlName);
        return funkoRepository.save(actualFunko);
    }
    public void onChange(Notificacion.Tipo tipo, Funko data) {
    log.debug("Servicio de productos onChange con tipo: " + tipo + " y datos: " + data);

    if (webSocketService == null) {
        log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
        webSocketService = this.webSocketConfig.webSocketFunkosHandler();
    }

    try {
        Notificacion<FunkoNotificacionResponseDto> notificacion = new Notificacion<>(
                "FUNKOS",
                tipo,
                funkoNotificacionMapper.toFunkoNotificacionMapper(data),
                LocalDateTime.now().toString()
        );

        String json = mapper.writeValueAsString((notificacion));

        log.info("Enviando mensaje a los clientes ws");
        // Enviamos el mensaje a los clientes ws con un hilo, si hay muchos clientes, puede tardar
        // no bloqueamos el hilo principal que atiende las peticiones http
        Thread senderThread = new Thread(() -> {
            try {
                webSocketService.sendMessage(json);
            } catch (Exception e) {
                log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
            }
        });
        senderThread.start();
    } catch (JsonProcessingException e) {
        log.error("Error al convertir la notificación a JSON", e);
    }
}
}
