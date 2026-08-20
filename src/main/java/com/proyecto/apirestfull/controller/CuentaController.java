package com.proyecto.apirestfull.controller;

import com.proyecto.apirestfull.dto.CuentaRequestDTO;
import com.proyecto.apirestfull.dto.CuentaResponseDTO;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> listar() {
        List<CuentaResponseDTO> cuentas = cuentaService.listarTodas().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(toResponseDTO(cuentaService.obtenerPorId(id)));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaResponseDTO>> obtenerPorCliente(@PathVariable Integer clienteId) {
        List<CuentaResponseDTO> cuentas = cuentaService.obtenerPorCliente(clienteId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> crear(@Valid @RequestBody CuentaRequestDTO request) {
        Cuenta creada = cuentaService.crear(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(creada));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<CuentaResponseDTO> activar(@PathVariable Integer id) {
        return ResponseEntity.ok(toResponseDTO(cuentaService.activar(id)));
    }

    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<CuentaResponseDTO> inactivar(@PathVariable Integer id) {
        return ResponseEntity.ok(toResponseDTO(cuentaService.inactivar(id)));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CuentaResponseDTO> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(toResponseDTO(cuentaService.cancelar(id)));
    }

    private Cuenta toEntity(CuentaRequestDTO dto) {
        return Cuenta.builder()
                .tipoCuenta(dto.getTipoCuenta())
                .saldo(dto.getSaldo())
                .exentaGmf(dto.getExentaGmf())
                .cliente(Cliente.builder().id(dto.getClienteId()).build())
                .build();
    }

    private CuentaResponseDTO toResponseDTO(Cuenta cuenta) {
        Cliente cliente = cuenta.getCliente();
        return CuentaResponseDTO.builder()
                .id(cuenta.getId())
                .tipoCuenta(cuenta.getTipoCuenta())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .estado(cuenta.getEstado())
                .saldo(cuenta.getSaldo())
                .saldoDisponible(cuenta.getSaldoDisponible())
                .exentaGmf(cuenta.getExentaGmf())
                .fechaCreacion(cuenta.getFechaCreacion())
                .fechaModificacion(cuenta.getFechaModificacion())
                .clienteId(cliente != null ? cliente.getId() : null)
                .clienteNombreCompleto(cliente != null ? cliente.getNombres() + " " + cliente.getApellidos() : null)
                .build();
    }
}
