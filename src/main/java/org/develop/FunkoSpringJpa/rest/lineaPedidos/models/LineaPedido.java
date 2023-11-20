package org.develop.FunkoSpringJpa.rest.lineaPedidos.models;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedido {
    @Builder.Default
    @Min(value = 1, message = "La cantidad del producto debe ser mayor a 0")
    private Integer cantidad = 1;
    private Long idFunko;
    @Builder.Default
    @Min(value = 0, message = "El precio del Producto debe ser mayor o igual a 0")
    private Double precioProducto = 0.0;
    @Builder.Default
    private Double total = 0.0;

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        this.total = this.cantidad * this.precioProducto;
    }

    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
        this.total = this.cantidad * this.precioProducto;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
