package org.develop.FunkoSpringJpa.rest.lineaPedidos.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoBadPrice;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoNotFound;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoNotStock;
import org.develop.FunkoSpringJpa.rest.funko.repositories.FunkoRepository;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions.PedidoEmptyException;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions.PedidoNotFoundException;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.commons.models.LineaPedido;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.commons.models.Pedido;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.repositories.PedidoRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@CacheConfig(cacheNames = {"pedidos"})
public class PedidoServiceImpl implements PedidoService{
    private final PedidoRepository pedidoRepository;
    private final FunkoRepository funkoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, FunkoRepository funkoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.funkoRepository = funkoRepository;
    }
    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        log.info("Obteniendo todos los pedidos ordenados segun: " + pageable);
        return pedidoRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public Pedido findById(ObjectId id) {
        log.info("Obteniendo Pedido con ID: " + id);
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFoundException("id "+ id.toHexString()));
    }

    @Override
    public Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable) {
        log.info(("Obteniendo pedidos de usuario con ID: " + idUsuario));
        return pedidoRepository.findByIdUsuario(idUsuario, pageable);
    }

    @Override
    @CachePut(key = "#pedido.id")
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido {}", pedido);

        checkPedido(pedido);

        var pedSave = reserveStockPedidos(pedido);

        pedSave.setCreatedAt(LocalDateTime.now());
        pedSave.setUpdatedAt(LocalDateTime.now());

        return pedidoRepository.save(pedSave);
    }
    @Override
    public Pedido update(ObjectId id, Pedido pedido) {
        log.info("Actualizando pedido {}", pedido);

        var pedidoupd = pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFoundException("id "+ id.toHexString()));

        returnStockPedidos(pedido);

        checkPedido(pedido);

        var pedUpd = reserveStockPedidos(pedido);
        pedUpd.setId(pedidoupd.getId());

        pedUpd.setUpdatedAt(LocalDateTime.now());

        return pedidoRepository.save(pedUpd);
    }

    @Override
    public void deleteById(ObjectId id) {
        log.info("Eliminando Pedido con ID: " + id);

        var pedDel = pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFoundException("id "+ id.toHexString()));

        returnStockPedidos(pedDel);

        pedidoRepository.deleteById(id);
    }

    Pedido reserveStockPedidos(Pedido pedido){
        log.info("Reservando stock del pedido: {}", pedido);

        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoEmptyException(pedido.get_Id());
        }

        pedido.getLineasPedido().forEach(lineaPedido -> {
            var funko = funkoRepository.findById(lineaPedido.getIdFunko()).orElseThrow(() -> new FunkoNotFound(lineaPedido.getIdFunko()));
            funko.setQuantity(funko.getQuantity() - lineaPedido.getCantidad());

            funkoRepository.save(funko);

            lineaPedido.setTotal(lineaPedido.getCantidad()* lineaPedido.getPrecioProducto());
        });

        var total = pedido.getLineasPedido().stream()
                .map(linea -> linea.getCantidad() * linea.getPrecioProducto())
                .reduce(0.0, Double::sum);

        var totalItems = pedido.getLineasPedido().stream()
                .map(LineaPedido::getCantidad)
                .reduce(0,Integer::sum);

        pedido.setTotal(total);
        pedido.setTotalItems(totalItems);

        return pedido;
    }
    void checkPedido(Pedido pedido){
        log.info("Comprobando pedido: {}", pedido);

        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoEmptyException(pedido.get_Id());
        }

        pedido.getLineasPedido().forEach(linea -> {
            var funko = funkoRepository.findById(linea.getIdFunko()).orElseThrow(() -> new FunkoNotFound(linea.getIdFunko()));

            if (funko.getQuantity() < linea.getCantidad() && linea.getCantidad() > 0) {
                throw new FunkoNotStock(linea.getIdFunko());
            }

            if (!funko.getPrice().equals(linea.getPrecioProducto())){
                throw new FunkoBadPrice(linea.getIdFunko());
            }
        });
    }
    Pedido returnStockPedidos(Pedido pedido){
        log.info("Devolviendo stock del pedido: {}", pedido);
        if (pedido.getLineasPedido() != null){
            pedido.getLineasPedido().forEach(linea -> {
                var funko = funkoRepository.findById(linea.getIdFunko()).orElseThrow(() -> new FunkoNotFound(linea.getIdFunko()));
                funko.setQuantity(funko.getQuantity() + linea.getCantidad());

                funkoRepository.save(funko);
            });
        }
        return pedido;
    }

}
