package com.proyecto.apirestfull.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private TipoCuenta tipoCuenta;

    @Column(name = "numero_cuenta", length = 10, nullable = false, unique = true, updatable = false)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCuenta estado;

    @Column(name = "saldo", precision = 15, scale = 2, nullable = false)
    private BigDecimal saldo;

    @Column(name = "saldo_disponible", precision = 15, scale = 2, nullable = false)
    private BigDecimal saldoDisponible;

    @Column(name = "exenta_gmf", nullable = false)
    private Boolean exentaGmf;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        fechaCreacion = ahora;
        fechaModificacion = ahora;
        if (estado == null) estado = EstadoCuenta.ACTIVA;
        if (exentaGmf == null) exentaGmf = false;
        if (saldo == null) saldo = BigDecimal.ZERO;
        if (saldoDisponible == null) saldoDisponible = saldo;
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    public enum TipoCuenta {
        CUENTA_AHORROS, CUENTA_CORRIENTE
    }

    public enum EstadoCuenta {
        ACTIVA, INACTIVA, CANCELADA
    }
}
