package com.proyecto.apirestfull.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto_financiero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductoFinanciero {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @NotBlank
    @Column(name = "nombre_producto", length = 80, nullable = false)
    private String nombreProducto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_producto", nullable = false)
    private TipoProducto tipoProducto;

    @Column(name = "tasa_interes", precision = 5, scale = 2)
    private BigDecimal tasaInteres;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "estado")
    private Boolean estado;

    @PrePersist
    public void prePersist() {
        if (estado == null) estado = true;
    }

    public enum TipoProducto {
        CUENTA_AHORROS, CUENTA_CORRIENTE, TARJETA_CREDITO, PRESTAMO, CDT
    }
   
}
