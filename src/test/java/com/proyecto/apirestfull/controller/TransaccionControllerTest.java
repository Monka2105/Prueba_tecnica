package com.proyecto.apirestfull.controller;

import tools.jackson.databind.ObjectMapper;
import com.proyecto.apirestfull.dto.TransaccionRequestDTO;
import com.proyecto.apirestfull.exception.SaldoInsuficienteException;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.model.Transaccion;
import com.proyecto.apirestfull.service.TransaccionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransaccionController.class)
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransaccionService transaccionService;

    @Test
    void registrar_consignacion_debeRetornar201() throws Exception {
        TransaccionRequestDTO request = TransaccionRequestDTO.builder()
                .cuentaId(1)
                .tipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION)
                .monto(BigDecimal.valueOf(200000))
                .build();

        Transaccion respuesta = Transaccion.builder()
                .id(1)
                .cuenta(Cuenta.builder().id(1).build())
                .tipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION)
                .tipoMovimiento(Transaccion.TipoMovimiento.CREDITO)
                .monto(BigDecimal.valueOf(200000))
                .saldoResultante(BigDecimal.valueOf(1700000))
                .build();

        when(transaccionService.registrar(any(Transaccion.class))).thenReturn(respuesta);

        mockMvc.perform(post("/api/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldoResultante").value(1700000));
    }

    @Test
    void registrar_retiroConSaldoInsuficiente_debeRetornar400() throws Exception {
        TransaccionRequestDTO request = TransaccionRequestDTO.builder()
                .cuentaId(1)
                .tipoTransaccion(Transaccion.TipoTransaccion.RETIRO)
                .monto(BigDecimal.valueOf(9999999))
                .build();

        when(transaccionService.registrar(any(Transaccion.class)))
                .thenThrow(new SaldoInsuficienteException("Saldo insuficiente en la cuenta 5300000001"));

        mockMvc.perform(post("/api/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
