package org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model;

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
public class Funko {
    public static final String IMG_DEFAULT = "https://via.placeholder.com/150";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name must not be blank")
    @Builder.Default
    private String name = "";
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Builder.Default
    private Double price = 0.0;
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Builder.Default
    private Integer quantity = 0;
    @Column(columnDefinition = "TEXT default '" + IMG_DEFAULT + "'")
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categoria category;
    @Builder.Default
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate createdAt = LocalDate.now();
    @Builder.Default
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDate updatedAt = LocalDate.now();

}
