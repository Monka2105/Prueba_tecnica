package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.SaldoInsuficienteException;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.model.Transaccion;
import com.proyecto.apirestfull.repository.CuentaRepository;
import com.proyecto.apirestfull.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock private TransaccionRepository transaccionRepository;
    @Mock private CuentaRepository cuentaRepository;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = Cuenta.builder().id(1).numeroCuenta("5300000001").saldo(BigDecimal.valueOf(1500000)).build();
    }

    @Test
    void registrar_consignacion_debeAumentarSaldo() {
        Transaccion tx = Transaccion.builder()
                .cuenta(Cuenta.builder().id(1).build())
                .tipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION)
                .monto(BigDecimal.valueOf(200000))
                .build();

        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaccion resultado = transaccionService.registrar(tx);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(Transaccion.TipoMovimiento.CREDITO);
        assertThat(resultado.getSaldoResultante()).isEqualByComparingTo(BigDecimal.valueOf(1700000));
    }

    @Test
    void registrar_retiroConSaldoInsuficiente_debeLanzarExcepcion() {
        Transaccion tx = Transaccion.builder()
                .cuenta(Cuenta.builder().id(1).build())
                .tipoTransaccion(Transaccion.TipoTransaccion.RETIRO)
                .monto(BigDecimal.valueOf(9999999))
                .build();

        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));

        assertThatThrownBy(() -> transaccionService.registrar(tx))
                .isInstanceOf(SaldoInsuficienteException.class);

        verify(transaccionRepository, never()).save(any());
    }

    @Test
    void registrar_transferencia_debeGenerarDosMovimientos() {
        Cuenta destino = Cuenta.builder().id(2).numeroCuenta("5300000002").saldo(BigDecimal.valueOf(300000)).build();

        Transaccion tx = Transaccion.builder()
                .cuenta(Cuenta.builder().id(1).build())
                .cuentaRelacionada(Cuenta.builder().id(2).build())
                .tipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA)
                .monto(BigDecimal.valueOf(200000))
                .build();

        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.findById(2)).thenReturn(Optional.of(destino));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaccion resultado = transaccionService.registrar(tx);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(Transaccion.TipoMovimiento.DEBITO);
        assertThat(resultado.getSaldoResultante()).isEqualByComparingTo(BigDecimal.valueOf(1300000));
        assertThat(destino.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(500000));
        // 2 movimientos: uno para la cuenta origen (debito) y otro para la destino (credito)
        verify(transaccionRepository, times(2)).save(any(Transaccion.class));
    }
}
