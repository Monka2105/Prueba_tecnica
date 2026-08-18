package com.proyecto.apirestfull.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cuenta {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Integer id;

    @NotBlank
    @Column(name = "numero_cuenta", length = 30, nullable = false, unique = true)
    private String numeroCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoFinanciero producto;

    @Column(name = "saldo", precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "fecha_apertura", updatable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "estado")
    private Boolean estado;

    @PrePersist
    public void prePersist() {
        if (fechaApertura == null) fechaApertura = LocalDateTime.now();
        if (estado == null) estado = true;
        if (saldo == null) saldo = BigDecimal.ZERO;
    }

    
}
