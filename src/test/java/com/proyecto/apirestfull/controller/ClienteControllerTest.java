package com.proyecto.apirestfull.controller;

import tools.jackson.databind.ObjectMapper;
import com.proyecto.apirestfull.dto.ClienteRequestDTO;
import com.proyecto.apirestfull.exception.ClienteConCuentasException;
import com.proyecto.apirestfull.exception.ClienteMenorEdadException;
import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    private Cliente clienteEjemplo() {
        return Cliente.builder()
                .id(1)
                .tipoDocumento("CC")
                .numeroDocumento("1075263841")
                .nombres("Laura")
                .apellidos("Martinez Rios")
                .email("laura.martinez@example.com")
                .fechaNacimiento(LocalDate.of(1995, 3, 14))
                .build();
    }

    private ClienteRequestDTO clienteRequestEjemplo() {
        return ClienteRequestDTO.builder()
                .tipoDocumento("CC")
                .numeroDocumento("1075263841")
                .nombres("Laura")
                .apellidos("Martinez Rios")
                .email("laura.martinez@example.com")
                .fechaNacimiento(LocalDate.of(1995, 3, 14))
                .build();
    }

    @Test
    void listar_debeRetornar200ConLista() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(clienteEjemplo()));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombres").value("Laura"));
    }

    @Test
    void obtener_cuandoExiste_debeRetornar200() throws Exception {
        when(clienteService.obtenerPorId(1)).thenReturn(clienteEjemplo());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroDocumento").value("1075263841"));
    }

    @Test
    void obtener_cuandoNoExiste_debeRetornar404() throws Exception {
        when(clienteService.obtenerPorId(99))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_conDatosValidos_debeRetornar201() throws Exception {
        ClienteRequestDTO nuevo = clienteRequestEjemplo();
        when(clienteService.crear(any(Cliente.class))).thenReturn(clienteEjemplo());

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crear_menorDeEdad_debeRetornar400() throws Exception {
        ClienteRequestDTO menor = clienteRequestEjemplo();
        menor.setFechaNacimiento(LocalDate.now().minusYears(10));
        when(clienteService.crear(any(Cliente.class)))
                .thenThrow(new ClienteMenorEdadException("El cliente debe ser mayor de edad (18 años) para ser registrado"));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menor)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_conCuentasVinculadas_debeRetornar409() throws Exception {
        doThrow(new ClienteConCuentasException("No se puede eliminar el cliente con id 1 porque tiene cuentas vinculadas"))
                .when(clienteService).eliminar(1);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isConflict());
    }

    @Test
    void eliminar_sinCuentas_debeRetornar204() throws Exception {
        doNothing().when(clienteService).eliminar(1);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}
