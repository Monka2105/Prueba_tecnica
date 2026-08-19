package com.proyecto.apirestfull.controller;

import tools.jackson.databind.ObjectMapper;
import com.proyecto.apirestfull.dto.CuentaRequestDTO;
import com.proyecto.apirestfull.exception.CuentaNoCancelableException;
import com.proyecto.apirestfull.exception.SaldoInvalidoException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.service.CuentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CuentaService cuentaService;

    private Cuenta cuentaEjemplo() {
        return Cuenta.builder()
                .id(1)
                .tipoCuenta(Cuenta.TipoCuenta.CUENTA_AHORROS)
                .numeroCuenta("5300000001")
                .estado(Cuenta.EstadoCuenta.ACTIVA)
                .saldo(BigDecimal.valueOf(1500000))
                .saldoDisponible(BigDecimal.valueOf(1500000))
                .exentaGmf(false)
                .cliente(Cliente.builder().id(1).build())
                .build();
    }

    private CuentaRequestDTO cuentaRequestEjemplo() {
        return CuentaRequestDTO.builder()
                .tipoCuenta(Cuenta.TipoCuenta.CUENTA_AHORROS)
                .saldo(BigDecimal.valueOf(1500000))
                .clienteId(1)
                .build();
    }

    @Test
    void listar_debeRetornar200() throws Exception {
        when(cuentaService.listarTodas()).thenReturn(List.of(cuentaEjemplo()));

        mockMvc.perform(get("/api/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("5300000001"));
    }

    @Test
    void crear_conDatosValidos_debeRetornar201() throws Exception {
        CuentaRequestDTO nueva = cuentaRequestEjemplo();
        when(cuentaService.crear(any(Cuenta.class))).thenReturn(cuentaEjemplo());

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("5300000001"));
    }

    @Test
    void crear_ahorrosConSaldoNegativo_debeRetornar400() throws Exception {
        CuentaRequestDTO invalida = cuentaRequestEjemplo();
        invalida.setSaldo(BigDecimal.valueOf(-100));
        when(cuentaService.crear(any(Cuenta.class)))
                .thenThrow(new SaldoInvalidoException("Una cuenta de ahorros no puede crearse con saldo negativo"));

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelar_conSaldoDistintoDeCero_debeRetornar409() throws Exception {
        when(cuentaService.cancelar(1))
                .thenThrow(new CuentaNoCancelableException("Solo se pueden cancelar cuentas con saldo en $0"));

        mockMvc.perform(patch("/api/cuentas/1/cancelar"))
                .andExpect(status().isConflict());
    }

    @Test
    void activar_debeRetornar200() throws Exception {
        Cuenta activada = cuentaEjemplo();
        activada.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        when(cuentaService.activar(1)).thenReturn(activada);

        mockMvc.perform(patch("/api/cuentas/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }
}
