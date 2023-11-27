package org.develop.FunkoSpringJpa.rest.lineaPedidos.commons.models;

import jakarta.persistence.EntityListeners;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document("pedidos")
@TypeAlias("Pedido")
@EntityListeners(AuditingEntityListener.class)
public class Pedido {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();
    @NotNull(message = "El Cliente no puede ser Nulo")
    private Cliente cliente;
    private Long idUsuario;
    @NotNull(message = "El pedido debe tener minimo una linea de pedido")
    private List<LineaPedido> lineasPedido;
    @Builder.Default
    private Integer totalItems = 0;
    @Builder.Default
    private Double total = 0.0;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    private Boolean deleted = false;

    public String get_Id(){
        return id.toHexString();
    }

    public void setLineasPedido(List<LineaPedido> lineasPed){
        this.lineasPedido = lineasPed;
        this.totalItems = lineasPed != null ? lineasPedido.size() : 0;
        this.total = lineasPed != null ? lineasPedido.stream().mapToDouble(LineaPedido::getTotal).sum() : 0.0;
    }
}
