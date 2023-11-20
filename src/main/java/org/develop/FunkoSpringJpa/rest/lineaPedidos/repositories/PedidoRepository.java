package org.develop.FunkoSpringJpa.rest.lineaPedidos.repositories;

import org.bson.types.ObjectId;
import org.develop.FunkoSpringJpa.rest.lineaPedidos.models.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, ObjectId> {
}
