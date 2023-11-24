package org.develop.FunkoSpringJpa.rest.lineaPedidos.services;

import org.bson.types.ObjectId;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {
    Page<Pedido> findAll(Pageable pageable);
    Pedido findById(ObjectId id);
    Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable);
    Pedido save(Pedido pedido);
    void deleteById(ObjectId id);
    Pedido update(ObjectId id,Pedido pedido);
}
