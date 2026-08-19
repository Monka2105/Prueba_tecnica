package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.ClienteConCuentasException;
import com.proyecto.apirestfull.exception.ClienteMenorEdadException;
import com.proyecto.apirestfull.exception.ResourceNotFoundException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private CuentaRepository cuentaRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteMayorDeEdad;

    @BeforeEach
    void setUp() {
        clienteMayorDeEdad = Cliente.builder()
                .id(1)
                .tipoDocumento("CC")
                .numeroDocumento("1075263841")
                .nombres("Laura")
                .apellidos("Martinez Rios")
                .email("laura.martinez@example.com")
                .fechaNacimiento(LocalDate.now().minusYears(30))
                .build();
    }

    @Test
    void listarTodos_debeRetornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(clienteMayorDeEdad));

        List<Cliente> resultado = clienteService.listarTodos();

        assertThat(resultado).hasSize(1);
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarResourceNotFoundException() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.obtenerPorId(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void crear_mayorDeEdad_debeGuardarCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteMayorDeEdad);

        Cliente resultado = clienteService.crear(clienteMayorDeEdad);

        assertThat(resultado.getId()).isEqualTo(1);
        verify(clienteRepository, times(1)).save(clienteMayorDeEdad);
    }

    @Test
    void crear_menorDeEdad_debeLanzarClienteMenorEdadException() {
        Cliente menor = Cliente.builder()
                .tipoDocumento("CC")
                .numeroDocumento("999")
                .nombres("Nino")
                .apellidos("Prueba")
                .email("nino@example.com")
                .fechaNacimiento(LocalDate.now().minusYears(10))
                .build();

        assertThatThrownBy(() -> clienteService.crear(menor))
                .isInstanceOf(ClienteMenorEdadException.class);

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void eliminar_sinCuentasVinculadas_debeEliminarCliente() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(clienteMayorDeEdad));
        when(cuentaRepository.findByClienteId(1)).thenReturn(List.of());

        clienteService.eliminar(1);

        verify(clienteRepository, times(1)).delete(clienteMayorDeEdad);
    }

    @Test
    void eliminar_conCuentasVinculadas_debeLanzarClienteConCuentasException() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(clienteMayorDeEdad));
        when(cuentaRepository.findByClienteId(1)).thenReturn(List.of(new Cuenta()));

        assertThatThrownBy(() -> clienteService.eliminar(1))
                .isInstanceOf(ClienteConCuentasException.class);

        verify(clienteRepository, never()).delete(any());
    }
}
