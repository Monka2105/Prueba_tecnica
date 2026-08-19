package com.proyecto.apirestfull.dto;

import com.proyecto.apirestfull.model.Transaccion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionRequestDTO {

    @NotNull
    private Integer cuentaId;

    private Integer cuentaRelacionadaId;

    @NotNull
    private Transaccion.TipoTransaccion tipoTransaccion;

    @NotNull
    @Positive
    private BigDecimal monto;

    private String descripcion;
}
