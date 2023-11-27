package org.develop.FunkoSpringJpa.rest.lineaPedidos.repositories;

import org.bson.types.ObjectId;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.commons.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, ObjectId> {
    Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable);
    List<Pedido> findPedidosByIdUsuario(Long idUsuario);
    boolean existsByIdUsuario(Long idUsuario);
}
