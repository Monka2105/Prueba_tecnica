package com.proyecto.apirestfull.dto;

import com.proyecto.apirestfull.model.Cuenta;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaRequestDTO {

    @NotNull
    private Cuenta.TipoCuenta tipoCuenta;

    private BigDecimal saldo;

    private Boolean exentaGmf;

    @NotNull
    private Integer clienteId;
}
