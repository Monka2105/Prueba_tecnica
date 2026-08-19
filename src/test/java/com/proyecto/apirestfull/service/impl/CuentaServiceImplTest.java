package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.CuentaNoCancelableException;
import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.exception.SaldoInvalidoException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.repository.ClienteRepository;
import com.proyecto.apirestfull.repository.CuentaRepository;
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
class CuentaServiceImplTest {

    @Mock private CuentaRepository cuentaRepository;
    @Mock private ClienteRepository clienteRepository;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder().id(1).nombres("Laura").build();
    }

    @Test
    void crear_cuentaAhorrosConSaldoPositivo_debeCrearse() {
        Cuenta nueva = Cuenta.builder()
                .tipoCuenta(Cuenta.TipoCuenta.CUENTA_AHORROS)
                .saldo(BigDecimal.valueOf(500000))
                .cliente(Cliente.builder().id(1).build())
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.existsByNumeroCuenta(any())).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));

        Cuenta resultado = cuentaService.crear(nueva);

        assertThat(resultado.getNumeroCuenta()).startsWith("53");
        assertThat(resultado.getEstado()).isEqualTo(Cuenta.EstadoCuenta.ACTIVA);
        assertThat(resultado.getSaldoDisponible()).isEqualByComparingTo(BigDecimal.valueOf(500000));
    }

    @Test
    void crear_cuentaAhorrosConSaldoNegativo_debeLanzarSaldoInvalidoException() {
        Cuenta nueva = Cuenta.builder()
                .tipoCuenta(Cuenta.TipoCuenta.CUENTA_AHORROS)
                .saldo(BigDecimal.valueOf(-1000))
                .cliente(Cliente.builder().id(1).build())
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        assertThatThrownBy(() -> cuentaService.crear(nueva))
                .isInstanceOf(SaldoInvalidoException.class);

        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void crear_cuentaCorriente_numeroDebeEmpezarCon33() {
        Cuenta nueva = Cuenta.builder()
                .tipoCuenta(Cuenta.TipoCuenta.CUENTA_CORRIENTE)
                .saldo(BigDecimal.valueOf(1000000))
                .cliente(Cliente.builder().id(1).build())
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.existsByNumeroCuenta(any())).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));

        Cuenta resultado = cuentaService.crear(nueva);

        assertThat(resultado.getNumeroCuenta()).startsWith("33");
        assertThat(resultado.getNumeroCuenta()).hasSize(10);
    }

    @Test
    void cancelar_conSaldoEnCero_debeCancelarse() {
        Cuenta cuenta = Cuenta.builder().id(1).saldo(BigDecimal.ZERO).estado(Cuenta.EstadoCuenta.ACTIVA).build();
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));

        Cuenta resultado = cuentaService.cancelar(1);

        assertThat(resultado.getEstado()).isEqualTo(Cuenta.EstadoCuenta.CANCELADA);
    }

    @Test
    void cancelar_conSaldoDistintoDeCero_debeLanzarCuentaNoCancelableException() {
        Cuenta cuenta = Cuenta.builder().id(1).saldo(BigDecimal.valueOf(50000)).estado(Cuenta.EstadoCuenta.ACTIVA).build();
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));

        assertThatThrownBy(() -> cuentaService.cancelar(1))
                .isInstanceOf(CuentaNoCancelableException.class);

        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(cuentaRepository.findById(77)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cuentaService.obtenerPorId(77))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
