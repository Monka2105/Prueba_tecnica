package com.proyecto.apirestfull.controller;

import com.proyecto.apirestfull.dto.TransaccionRequestDTO;
import com.proyecto.apirestfull.dto.TransaccionResponseDTO;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.model.Transaccion;
import com.proyecto.apirestfull.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDTO>> listar() {
        List<TransaccionResponseDTO> transacciones = transaccionService.listarTodas().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(toResponseDTO(transaccionService.obtenerPorId(id)));
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPorCuenta(@PathVariable Integer cuentaId) {
        List<TransaccionResponseDTO> transacciones = transaccionService.obtenerPorCuenta(cuentaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transacciones);
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> registrar(@Valid @RequestBody TransaccionRequestDTO request) {
        Transaccion registrada = transaccionService.registrar(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(registrada));
    }

    private Transaccion toEntity(TransaccionRequestDTO dto) {
        return Transaccion.builder()
                .cuenta(Cuenta.builder().id(dto.getCuentaId()).build())
                .cuentaRelacionada(dto.getCuentaRelacionadaId() != null
                        ? Cuenta.builder().id(dto.getCuentaRelacionadaId()).build()
                        : null)
                .tipoTransaccion(dto.getTipoTransaccion())
                .monto(dto.getMonto())
                .descripcion(dto.getDescripcion())
                .build();
    }

    private TransaccionResponseDTO toResponseDTO(Transaccion transaccion) {
        return TransaccionResponseDTO.builder()
                .id(transaccion.getId())
                .cuentaId(transaccion.getCuenta() != null ? transaccion.getCuenta().getId() : null)
                .cuentaRelacionadaId(transaccion.getCuentaRelacionada() != null
                        ? transaccion.getCuentaRelacionada().getId() : null)
                .tipoTransaccion(transaccion.getTipoTransaccion())
                .tipoMovimiento(transaccion.getTipoMovimiento())
                .monto(transaccion.getMonto())
                .saldoResultante(transaccion.getSaldoResultante())
                .fechaTransaccion(transaccion.getFechaTransaccion())
                .descripcion(transaccion.getDescripcion())
                .build();
    }
}
