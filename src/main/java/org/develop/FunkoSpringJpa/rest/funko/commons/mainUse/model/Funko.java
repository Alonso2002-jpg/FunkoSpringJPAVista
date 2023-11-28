package org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FUNKOS")
@Schema(name = "Funko")
public class Funko {
    public static final String IMG_DEFAULT = "https://via.placeholder.com/150";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del funko", example = "1")
    private Long id;
    @NotBlank(message = "Name must not be blank")
    @Builder.Default
    @Schema(description = "Nombre del funko", example = "Funko de Amatista")
    private String name = "";
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Builder.Default
    @Schema(description = "Precio del funko", example = "100.0")
    private Double price = 0.0;
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Builder.Default
    @Schema(description = "Cantidad de funko", example = "10")
    private Integer quantity = 0;
    @Column(columnDefinition = "TEXT default '" + IMG_DEFAULT + "'")
    @Schema(description = "Imagen del funko", example = "https://via.placeholder.com/150")
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "Categoría del funko", example = "DISNEY")
    private Categoria category;
    @Builder.Default
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creación del funko", example = "2022-01-01")
    private LocalDate createdAt = LocalDate.now();
    @Builder.Default
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualización del funko", example = "2022-01-01")
    private LocalDate updatedAt = LocalDate.now();

}
