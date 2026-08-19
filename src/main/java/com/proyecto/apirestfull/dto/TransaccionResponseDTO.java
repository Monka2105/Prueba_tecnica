package com.proyecto.apirestfull.dto;

import com.proyecto.apirestfull.model.Transaccion;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionResponseDTO {

    private Integer id;
    private Integer cuentaId;
    private Integer cuentaRelacionadaId;
    private Transaccion.TipoTransaccion tipoTransaccion;
    private Transaccion.TipoMovimiento tipoMovimiento;
    private BigDecimal monto;
    private BigDecimal saldoResultante;
    private LocalDateTime fechaTransaccion;
    private String descripcion;
}
