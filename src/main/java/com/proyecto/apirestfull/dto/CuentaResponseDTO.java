package com.proyecto.apirestfull.dto;

import com.proyecto.apirestfull.model.Cuenta;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaResponseDTO {

    private Integer id;
    private Cuenta.TipoCuenta tipoCuenta;
    private String numeroCuenta;
    private Cuenta.EstadoCuenta estado;
    private BigDecimal saldo;
    private BigDecimal saldoDisponible;
    private Boolean exentaGmf;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Integer clienteId;
    private String clienteNombreCompleto;
}
